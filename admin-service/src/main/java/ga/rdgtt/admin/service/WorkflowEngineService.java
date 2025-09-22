package ga.rdgtt.admin.service;

import ga.rdgtt.admin.model.*;
import ga.rdgtt.admin.repository.WorkflowInstanceRepository;
import ga.rdgtt.admin.repository.WorkflowStepExecutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class WorkflowEngineService {
    
    @Autowired
    private WorkflowInstanceRepository workflowInstanceRepository;
    
    @Autowired
    private WorkflowStepExecutionRepository workflowStepExecutionRepository;
    
    @Autowired
    private ProcessStepService processStepService;
    
    @Autowired
    private DocumentTypeService documentTypeService;
    
    /**
     * Démarrer un nouveau workflow pour une demande
     */
    public WorkflowInstance startWorkflow(String demandeId, UUID documentTypeId, String contexteData) {
        DocumentType documentType = documentTypeService.findById(documentTypeId)
                .orElseThrow(() -> new RuntimeException("Type de document non trouvé"));
        
        WorkflowInstance workflow = new WorkflowInstance(demandeId, documentType);
        workflow.setDonneesContexte(contexteData);
        workflow.setDelaiMaxJours(documentType.getDelaiTraitementJours());
        
        WorkflowInstance savedWorkflow = workflowInstanceRepository.save(workflow);
        
        // Démarrer la première étape
        startNextStep(savedWorkflow);
        
        return savedWorkflow;
    }
    
    /**
     * Exécuter une étape du workflow
     */
    public WorkflowStepExecution executeStep(UUID workflowInstanceId, UUID processStepId, 
                                           UUID utilisateurId, String decision, 
                                           String commentaires, String donneesValidation) {
        
        WorkflowInstance workflow = workflowInstanceRepository.findById(workflowInstanceId)
                .orElseThrow(() -> new RuntimeException("Workflow non trouvé"));
        
        ProcessStep processStep = processStepService.findById(processStepId)
                .orElseThrow(() -> new RuntimeException("Étape de processus non trouvée"));
        
        // Créer l'exécution de l'étape
        WorkflowStepExecution execution = new WorkflowStepExecution(workflow, processStep);
        execution.setUtilisateurId(utilisateurId);
        execution.setDecision(decision);
        execution.setCommentaires(commentaires);
        execution.setDonneesValidation(donneesValidation);
        execution.setDateDebut(LocalDateTime.now());
        execution.setStatut("EN_COURS");
        
        WorkflowStepExecution savedExecution = workflowStepExecutionRepository.save(execution);
        
        // Traiter la décision
        processDecision(workflow, processStep, decision, savedExecution);
        
        return savedExecution;
    }
    
    /**
     * Traiter la décision d'une étape
     */
    private void processDecision(WorkflowInstance workflow, ProcessStep processStep, 
                               String decision, WorkflowStepExecution execution) {
        
        execution.setDateFin(LocalDateTime.now());
        
        switch (decision.toUpperCase()) {
            case "VALIDE":
                execution.setStatut("VALIDE");
                workflow.setStatut("EN_COURS");
                
                // Passer à l'étape suivante
                if (processStep.getEtapeSuivanteId() != null) {
                    workflow.setEtapeActuelleId(processStep.getEtapeSuivanteId());
                    startNextStep(workflow);
                } else {
                    // Dernière étape - workflow terminé
                    workflow.setStatut("VALIDE");
                    workflow.setDateFin(LocalDateTime.now());
                }
                break;
                
            case "REJETE":
                execution.setStatut("REJETE");
                workflow.setStatut("REJETE");
                workflow.setDateFin(LocalDateTime.now());
                break;
                
            case "REDIRIGE":
                execution.setStatut("VALIDE");
                workflow.setStatut("EN_COURS");
                // Rediriger vers l'étape spécifiée
                if (execution.getEtapeSuivanteId() != null) {
                    workflow.setEtapeActuelleId(execution.getEtapeSuivanteId());
                    startNextStep(workflow);
                }
                break;
        }
        
        workflowInstanceRepository.save(workflow);
        workflowStepExecutionRepository.save(execution);
    }
    
    /**
     * Démarrer l'étape suivante
     */
    private void startNextStep(WorkflowInstance workflow) {
        if (workflow.getEtapeActuelleId() != null) {
            ProcessStep nextStep = processStepService.findById(workflow.getEtapeActuelleId())
                    .orElseThrow(() -> new RuntimeException("Étape suivante non trouvée"));
            
            // Créer l'exécution de l'étape suivante
            WorkflowStepExecution nextExecution = new WorkflowStepExecution(workflow, nextStep);
            nextExecution.setStatut("EN_ATTENTE");
            nextExecution.setDateDebut(LocalDateTime.now());
            
            // Assigner à l'utilisateur/département/bureau approprié
            nextExecution.setDepartementId(nextStep.getDepartementId());
            nextExecution.setBureauId(nextStep.getBureauId());
            
            workflowStepExecutionRepository.save(nextExecution);
        }
    }
    
    /**
     * Obtenir le statut d'un workflow
     */
    public Optional<WorkflowInstance> getWorkflowStatus(UUID workflowInstanceId) {
        return workflowInstanceRepository.findById(workflowInstanceId);
    }
    
    /**
     * Obtenir l'historique d'un workflow
     */
    public List<WorkflowStepExecution> getWorkflowHistory(UUID workflowInstanceId) {
        return workflowStepExecutionRepository.findByWorkflowInstanceIdOrderByCreatedAtAsc(workflowInstanceId);
    }
    
    /**
     * Obtenir les workflows en attente pour un utilisateur
     */
    public List<WorkflowInstance> getPendingWorkflowsForUser(UUID utilisateurId) {
        return workflowInstanceRepository.findPendingWorkflowsForUser(utilisateurId);
    }
    
    /**
     * Obtenir les workflows en attente pour un département
     */
    public List<WorkflowInstance> getPendingWorkflowsForDepartment(UUID departementId) {
        return workflowInstanceRepository.findPendingWorkflowsForDepartment(departementId);
    }
    
    /**
     * Obtenir les workflows en attente pour un bureau
     */
    public List<WorkflowInstance> getPendingWorkflowsForBureau(UUID bureauId) {
        return workflowInstanceRepository.findPendingWorkflowsForBureau(bureauId);
    }
    
    /**
     * Annuler un workflow
     */
    public WorkflowInstance cancelWorkflow(UUID workflowInstanceId, String raison) {
        WorkflowInstance workflow = workflowInstanceRepository.findById(workflowInstanceId)
                .orElseThrow(() -> new RuntimeException("Workflow non trouvé"));
        
        workflow.setStatut("ANNULE");
        workflow.setDateFin(LocalDateTime.now());
        workflow.setCommentaires(raison);
        
        return workflowInstanceRepository.save(workflow);
    }
}
