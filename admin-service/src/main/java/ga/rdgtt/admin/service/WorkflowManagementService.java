package ga.rdgtt.admin.service;

import ga.rdgtt.admin.model.*;
import ga.rdgtt.admin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class WorkflowManagementService {
    
    @Autowired
    private DocumentTypeRepository documentTypeRepository;
    
    @Autowired
    private ProcessStepRepository processStepRepository;
    
    @Autowired
    private WorkflowInstanceRepository workflowInstanceRepository;
    
    @Autowired
    private WorkflowStepExecutionRepository workflowStepExecutionRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private BureauRepository bureauRepository;
    
    /**
     * Create a new document type with workflow configuration
     */
    public DocumentType createDocumentType(DocumentType documentType) {
        return documentTypeRepository.save(documentType);
    }
    
    /**
     * Create a new process step for a document type
     */
    public ProcessStep createProcessStep(ProcessStep processStep) {
        // Validate that the step order is unique within the document type
        validateStepOrder(processStep);
        return processStepRepository.save(processStep);
    }
    
    /**
     * Create a complete workflow for a document type
     */
    public DocumentType createWorkflow(DocumentType documentType, List<ProcessStep> steps) {
        // Save document type first
        DocumentType savedDocType = documentTypeRepository.save(documentType);
        
        // Save all process steps
        for (ProcessStep step : steps) {
            step.setDocumentType(savedDocType);
            processStepRepository.save(step);
        }
        
        return savedDocType;
    }
    
    /**
     * Start a new workflow instance
     */
    public WorkflowInstance startWorkflow(String demandeId, UUID documentTypeId, AdminUser currentUser) {
        DocumentType documentType = documentTypeRepository.findById(documentTypeId)
                .orElseThrow(() -> new RuntimeException("Type de document non trouvé"));
        
        // Get the first step
        List<ProcessStep> steps = processStepRepository.findByDocumentTypeIdOrderByOrdre(documentTypeId);
        if (steps.isEmpty()) {
            throw new RuntimeException("Aucune étape définie pour ce type de document");
        }
        
        ProcessStep firstStep = steps.get(0);
        
        // Create workflow instance
        WorkflowInstance instance = new WorkflowInstance();
        instance.setDemandeId(demandeId);
        instance.setDocumentType(documentType);
        instance.setStatut("EN_COURS");
        instance.setEtapeActuelleId(firstStep.getId());
        instance.setUtilisateurActuelId(currentUser.getId());
        instance.setDepartementActuelId(firstStep.getDepartementId());
        instance.setBureauActuelId(firstStep.getBureauId());
        instance.setDateDebut(LocalDateTime.now());
        instance.setDelaiMaxJours(documentType.getDelaiTraitementJours());
        
        return workflowInstanceRepository.save(instance);
    }
    
    /**
     * Execute a workflow step
     */
    public WorkflowStepExecution executeStep(UUID workflowInstanceId, UUID stepId, 
                                           String decision, String commentaires, 
                                           AdminUser currentUser) {
        
        WorkflowInstance instance = workflowInstanceRepository.findById(workflowInstanceId)
                .orElseThrow(() -> new RuntimeException("Instance de workflow non trouvée"));
        
        ProcessStep step = processStepRepository.findById(stepId)
                .orElseThrow(() -> new RuntimeException("Étape non trouvée"));
        
        // Create step execution
        WorkflowStepExecution execution = new WorkflowStepExecution();
        execution.setWorkflowInstanceId(workflowInstanceId);
        execution.setProcessStepId(stepId);
        execution.setStatut("TERMINE");
        execution.setUtilisateurId(currentUser.getId());
        execution.setDepartementId(step.getDepartementId());
        execution.setBureauId(step.getBureauId());
        execution.setDateDebut(LocalDateTime.now());
        execution.setDateFin(LocalDateTime.now());
        execution.setCommentaires(commentaires);
        execution.setDecision(decision);
        
        // Determine next step
        if ("APPROUVE".equals(decision)) {
            ProcessStep nextStep = getNextStep(step);
            if (nextStep != null) {
                execution.setEtapeSuivanteId(nextStep.getId());
                // Update workflow instance
                instance.setEtapeActuelleId(nextStep.getId());
                instance.setUtilisateurActuelId(null); // Will be assigned when next user takes it
                instance.setDepartementActuelId(nextStep.getDepartementId());
                instance.setBureauActuelId(nextStep.getBureauId());
            } else {
                // Workflow completed
                instance.setStatut("TERMINE");
                instance.setDateFin(LocalDateTime.now());
            }
        } else if ("REJETTE".equals(decision)) {
            instance.setStatut("REJETE");
            instance.setDateFin(LocalDateTime.now());
        }
        
        workflowInstanceRepository.save(instance);
        return workflowStepExecutionRepository.save(execution);
    }
    
    /**
     * Get all document types
     */
    public List<DocumentType> getAllDocumentTypes() {
        return documentTypeRepository.findAll();
    }
    
    /**
     * Get all process steps for a document type
     */
    public List<ProcessStep> getProcessSteps(UUID documentTypeId) {
        return processStepRepository.findByDocumentTypeIdOrderByOrdre(documentTypeId);
    }
    
    /**
     * Get workflow instance by demande ID
     */
    public Optional<WorkflowInstance> getWorkflowByDemandeNumber(String demandeNumber) {
        List<WorkflowInstance> workflows = workflowInstanceRepository.findByDemandeId(demandeNumber);
        return workflows.isEmpty() ? Optional.empty() : Optional.of(workflows.get(0));
    }
    
    /**
     * Get workflow history
     */
    public List<WorkflowStepExecution> getWorkflowHistory(UUID workflowInstanceId) {
        return workflowStepExecutionRepository.findByWorkflowInstanceIdOrderByDateDebut(workflowInstanceId);
    }
    
    /**
     * Get workflows assigned to a user
     */
    public List<WorkflowInstance> getWorkflowsForUser(UUID userId) {
        return workflowInstanceRepository.findByUtilisateurActuelId(userId);
    }
    
    /**
     * Get workflows for a department
     */
    public List<WorkflowInstance> getWorkflowsForDepartment(UUID departmentId) {
        return workflowInstanceRepository.findByDepartementActuelId(departmentId);
    }
    
    /**
     * Get workflows for a bureau
     */
    public List<WorkflowInstance> getWorkflowsForBureau(UUID bureauId) {
        return workflowInstanceRepository.findByBureauActuelId(bureauId);
    }
    
    // Private helper methods
    private void validateStepOrder(ProcessStep step) {
        List<ProcessStep> existingSteps = processStepRepository
                .findByDocumentTypeIdOrderByOrdre(step.getDocumentTypeId());
        
        boolean orderExists = existingSteps.stream()
                .anyMatch(s -> s.getOrdre().equals(step.getOrdre()));
        
        if (orderExists) {
            throw new RuntimeException("L'ordre " + step.getOrdre() + " existe déjà pour ce type de document");
        }
    }
    
    private ProcessStep getNextStep(ProcessStep currentStep) {
        List<ProcessStep> allSteps = processStepRepository
                .findByDocumentTypeIdOrderByOrdre(currentStep.getDocumentTypeId());
        
        int currentIndex = allSteps.indexOf(currentStep);
        if (currentIndex < allSteps.size() - 1) {
            return allSteps.get(currentIndex + 1);
        }
        
        return null;
    }
}


