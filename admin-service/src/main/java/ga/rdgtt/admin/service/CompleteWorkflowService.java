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
public class CompleteWorkflowService {
    
    @Autowired
    private WorkflowManagementService workflowManagementService;
    
    @Autowired
    private DocumentTypeRepository documentTypeRepository;
    
    @Autowired
    private ProcessStepRepository processStepRepository;
    
    @Autowired
    private WorkflowInstanceRepository workflowInstanceRepository;
    
    @Autowired
    private AdminUserRepository adminUserRepository;
    
    /**
     * Start Auto-École Registration Workflow
     */
    public WorkflowInstance startAutoEcoleRegistration(String autoEcoleName, String demandeId, UUID userId) {
        DocumentType docType = documentTypeRepository.findByCode("INSCRIPTION_AUTO_ECOLE")
                .orElseThrow(() -> new RuntimeException("Type de document INSCRIPTION_AUTO_ECOLE non trouvé"));
        
        AdminUser user = adminUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        String contexteData = String.format("{\"auto_ecole_name\": \"%s\", \"type\": \"auto_ecole_registration\"}", autoEcoleName);
        
        WorkflowInstance instance = new WorkflowInstance();
        instance.setDemandeId(demandeId);
        instance.setDocumentType(docType);
        instance.setStatut("EN_COURS");
        instance.setDateDebut(LocalDateTime.now());
        instance.setDelaiMaxJours(docType.getDelaiTraitementJours());
        instance.setDonneesContexte(contexteData);
        
        // Set first step
        List<ProcessStep> steps = processStepRepository.findByDocumentTypeIdOrderByOrdre(docType.getId());
        if (!steps.isEmpty()) {
            ProcessStep firstStep = steps.get(0);
            instance.setEtapeActuelleId(firstStep.getId());
            instance.setDepartementActuelId(firstStep.getDepartementId());
            instance.setBureauActuelId(firstStep.getBureauId());
        }
        
        return workflowInstanceRepository.save(instance);
    }
    
    /**
     * Start Candidate Registration Workflow
     */
    public WorkflowInstance startCandidateRegistration(String candidateName, String autoEcoleId, String demandeId, UUID userId) {
        DocumentType docType = documentTypeRepository.findByCode("INSCRIPTION_CANDIDAT")
                .orElseThrow(() -> new RuntimeException("Type de document INSCRIPTION_CANDIDAT non trouvé"));
        
        AdminUser user = adminUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        String contexteData = String.format("{\"candidate_name\": \"%s\", \"auto_ecole_id\": \"%s\", \"type\": \"candidate_registration\"}", 
                candidateName, autoEcoleId);
        
        WorkflowInstance instance = new WorkflowInstance();
        instance.setDemandeId(demandeId);
        instance.setDocumentType(docType);
        instance.setStatut("EN_COURS");
        instance.setDateDebut(LocalDateTime.now());
        instance.setDelaiMaxJours(docType.getDelaiTraitementJours());
        instance.setDonneesContexte(contexteData);
        
        // Set first step
        List<ProcessStep> steps = processStepRepository.findByDocumentTypeIdOrderByOrdre(docType.getId());
        if (!steps.isEmpty()) {
            ProcessStep firstStep = steps.get(0);
            instance.setEtapeActuelleId(firstStep.getId());
            instance.setDepartementActuelId(firstStep.getDepartementId());
            instance.setBureauActuelId(firstStep.getBureauId());
        }
        
        return workflowInstanceRepository.save(instance);
    }
    
    /**
     * Start Permis de Conduire Workflow (Main Process)
     */
    public WorkflowInstance startPermisConduire(String candidateId, String demandeId, UUID userId) {
        DocumentType docType = documentTypeRepository.findByCode("PERMIS_CONDUIRE")
                .orElseThrow(() -> new RuntimeException("Type de document PERMIS_CONDUIRE non trouvé"));
        
        AdminUser user = adminUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        String contexteData = String.format("{\"candidate_id\": \"%s\", \"type\": \"permis_conduire\"}", candidateId);
        
        WorkflowInstance instance = new WorkflowInstance();
        instance.setDemandeId(demandeId);
        instance.setDocumentType(docType);
        instance.setStatut("EN_COURS");
        instance.setDateDebut(LocalDateTime.now());
        instance.setDelaiMaxJours(docType.getDelaiTraitementJours());
        instance.setDonneesContexte(contexteData);
        
        // Set first step
        List<ProcessStep> steps = processStepRepository.findByDocumentTypeIdOrderByOrdre(docType.getId());
        if (!steps.isEmpty()) {
            ProcessStep firstStep = steps.get(0);
            instance.setEtapeActuelleId(firstStep.getId());
            instance.setDepartementActuelId(firstStep.getDepartementId());
            instance.setBureauActuelId(firstStep.getBureauId());
        }
        
        return workflowInstanceRepository.save(instance);
    }
    
    /**
     * Start Connexe Workflow (Duplicata, Renouvellement, etc.)
     */
    public WorkflowInstance startConnexeWorkflow(String connexeType, String permisId, String demandeId, UUID userId) {
        DocumentType docType = documentTypeRepository.findByCode(connexeType)
                .orElseThrow(() -> new RuntimeException("Type de document " + connexeType + " non trouvé"));
        
        AdminUser user = adminUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        String contexteData = String.format("{\"permis_id\": \"%s\", \"connexe_type\": \"%s\", \"type\": \"connexe\"}", 
                permisId, connexeType);
        
        WorkflowInstance instance = new WorkflowInstance();
        instance.setDemandeId(demandeId);
        instance.setDocumentType(docType);
        instance.setStatut("EN_COURS");
        instance.setDateDebut(LocalDateTime.now());
        instance.setDelaiMaxJours(docType.getDelaiTraitementJours());
        instance.setDonneesContexte(contexteData);
        
        // Set first step
        List<ProcessStep> steps = processStepRepository.findByDocumentTypeIdOrderByOrdre(docType.getId());
        if (!steps.isEmpty()) {
            ProcessStep firstStep = steps.get(0);
            instance.setEtapeActuelleId(firstStep.getId());
            instance.setDepartementActuelId(firstStep.getDepartementId());
            instance.setBureauActuelId(firstStep.getBureauId());
        }
        
        return workflowInstanceRepository.save(instance);
    }
    
    /**
     * Get all available document types
     */
    public List<DocumentType> getAllDocumentTypes() {
        return documentTypeRepository.findAll();
    }
    
    /**
     * Get document types by service
     */
    public List<DocumentType> getDocumentTypesByService(String serviceCode) {
        return documentTypeRepository.findByServiceCode(serviceCode);
    }
    
    /**
     * Get document types by category
     */
    public List<DocumentType> getDocumentTypesByCategory(String category) {
        return documentTypeRepository.findByCategorie(category);
    }
    
    /**
     * Get workflow instance by demande ID
     */
    public Optional<WorkflowInstance> getWorkflowByDemandeNumber(String demandeNumber) {
        return workflowInstanceRepository.findByDemandeId(demandeNumber);
    }
    
    /**
     * Get all workflow instances
     */
    public List<WorkflowInstance> getAllWorkflowInstances() {
        return workflowInstanceRepository.findAll();
    }
    
    /**
     * Get workflow instances by status
     */
    public List<WorkflowInstance> getWorkflowInstancesByStatus(String status) {
        return workflowInstanceRepository.findByStatut(status);
    }
    
    /**
     * Get workflow instances by document type
     */
    public List<WorkflowInstance> getWorkflowInstancesByDocumentType(String documentTypeCode) {
        DocumentType docType = documentTypeRepository.findByCode(documentTypeCode)
                .orElseThrow(() -> new RuntimeException("Type de document non trouvé"));
        
        return workflowInstanceRepository.findByDocumentTypeId(docType.getId());
    }
    
    /**
     * Get workflow statistics
     */
    public Object getWorkflowStatistics() {
        long totalWorkflows = workflowInstanceRepository.count();
        long enCours = workflowInstanceRepository.countByStatut("EN_COURS");
        long termines = workflowInstanceRepository.countByStatut("TERMINE");
        long rejetes = workflowInstanceRepository.countByStatut("REJETE");
        
        return new Object() {
            public final long total = totalWorkflows;
            public final long enCours = enCours;
            public final long termines = termines;
            public final long rejetes = rejetes;
        };
    }
    
    /**
     * Get process steps for a document type
     */
    public List<ProcessStep> getProcessSteps(String documentTypeCode) {
        DocumentType docType = documentTypeRepository.findByCode(documentTypeCode)
                .orElseThrow(() -> new RuntimeException("Type de document non trouvé"));
        
        return processStepRepository.findByDocumentTypeIdOrderByOrdre(docType.getId());
    }
}


