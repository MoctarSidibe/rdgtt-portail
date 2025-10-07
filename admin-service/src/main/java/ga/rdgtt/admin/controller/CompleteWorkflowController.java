package ga.rdgtt.admin.controller;

import ga.rdgtt.admin.model.*;
import ga.rdgtt.admin.service.CompleteWorkflowService;
import ga.rdgtt.admin.service.WorkflowManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/workflow")
@CrossOrigin(origins = "*")
public class CompleteWorkflowController {
    
    @Autowired
    private CompleteWorkflowService completeWorkflowService;
    
    @Autowired
    private WorkflowManagementService workflowManagementService;
    
    // ==============================================
    // WORKFLOW INITIATION
    // ==============================================
    
    /**
     * Start Auto-École Registration Workflow
     */
    @PostMapping("/auto-ecole/start")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<WorkflowInstance> startAutoEcoleRegistration(
            @RequestParam String autoEcoleName,
            @RequestParam String demandeId,
            @RequestParam UUID userId) {
        
        WorkflowInstance instance = completeWorkflowService.startAutoEcoleRegistration(autoEcoleName, demandeId, userId);
        return ResponseEntity.ok(instance);
    }
    
    /**
     * Start Candidate Registration Workflow
     */
    @PostMapping("/candidate/start")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<WorkflowInstance> startCandidateRegistration(
            @RequestParam String candidateName,
            @RequestParam String autoEcoleId,
            @RequestParam String demandeId,
            @RequestParam UUID userId) {
        
        WorkflowInstance instance = completeWorkflowService.startCandidateRegistration(candidateName, autoEcoleId, demandeId, userId);
        return ResponseEntity.ok(instance);
    }
    
    /**
     * Start Permis de Conduire Workflow
     */
    @PostMapping("/permis/start")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<WorkflowInstance> startPermisConduire(
            @RequestParam String candidateId,
            @RequestParam String demandeId,
            @RequestParam UUID userId) {
        
        WorkflowInstance instance = completeWorkflowService.startPermisConduire(candidateId, demandeId, userId);
        return ResponseEntity.ok(instance);
    }
    
    /**
     * Start Connexe Workflow (Duplicata, Renouvellement, etc.)
     */
    @PostMapping("/connexe/start")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<WorkflowInstance> startConnexeWorkflow(
            @RequestParam String connexeType,
            @RequestParam String permisId,
            @RequestParam String demandeId,
            @RequestParam UUID userId) {
        
        WorkflowInstance instance = completeWorkflowService.startConnexeWorkflow(connexeType, permisId, demandeId, userId);
        return ResponseEntity.ok(instance);
    }
    
    // ==============================================
    // WORKFLOW EXECUTION
    // ==============================================
    
    /**
     * Execute a workflow step
     */
    @PostMapping("/execute")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<WorkflowStepExecution> executeStep(
            @RequestParam UUID workflowId,
            @RequestParam UUID stepId,
            @RequestParam String decision,
            @RequestParam(required = false) String commentaires,
            @RequestParam UUID userId) {
        
        WorkflowStepExecution execution = workflowManagementService.executeStep(workflowId, stepId, decision, commentaires, 
                completeWorkflowService.getAdminUserById(userId));
        return ResponseEntity.ok(execution);
    }
    
    // ==============================================
    // WORKFLOW QUERIES
    // ==============================================
    
    /**
     * Get all document types
     */
    @GetMapping("/document-types")
    public ResponseEntity<List<DocumentType>> getAllDocumentTypes() {
        return ResponseEntity.ok(completeWorkflowService.getAllDocumentTypes());
    }
    
    /**
     * Get document types by service
     */
    @GetMapping("/document-types/service/{serviceCode}")
    public ResponseEntity<List<DocumentType>> getDocumentTypesByService(@PathVariable String serviceCode) {
        return ResponseEntity.ok(completeWorkflowService.getDocumentTypesByService(serviceCode));
    }
    
    /**
     * Get document types by category
     */
    @GetMapping("/document-types/category/{category}")
    public ResponseEntity<List<DocumentType>> getDocumentTypesByCategory(@PathVariable String category) {
        return ResponseEntity.ok(completeWorkflowService.getDocumentTypesByCategory(category));
    }
    
    /**
     * Get process steps for a document type
     */
    @GetMapping("/document-types/{documentTypeCode}/steps")
    public ResponseEntity<List<ProcessStep>> getProcessSteps(@PathVariable String documentTypeCode) {
        return ResponseEntity.ok(completeWorkflowService.getProcessSteps(documentTypeCode));
    }
    
    /**
     * Get workflow instance by demande number
     */
    @GetMapping("/instance/{demandeNumber}")
    public ResponseEntity<WorkflowInstance> getWorkflowByDemandeNumber(@PathVariable String demandeNumber) {
        return completeWorkflowService.getWorkflowByDemandeNumber(demandeNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get all workflow instances
     */
    @GetMapping("/instances")
    @PreAuthorize("hasRole('CHEF_SERVICE')")
    public ResponseEntity<List<WorkflowInstance>> getAllWorkflowInstances() {
        return ResponseEntity.ok(completeWorkflowService.getAllWorkflowInstances());
    }
    
    /**
     * Get workflow instances by status
     */
    @GetMapping("/instances/status/{status}")
    @PreAuthorize("hasRole('CHEF_SERVICE')")
    public ResponseEntity<List<WorkflowInstance>> getWorkflowInstancesByStatus(@PathVariable String status) {
        return ResponseEntity.ok(completeWorkflowService.getWorkflowInstancesByStatus(status));
    }
    
    /**
     * Get workflow instances by document type
     */
    @GetMapping("/instances/document-type/{documentTypeCode}")
    @PreAuthorize("hasRole('CHEF_SERVICE')")
    public ResponseEntity<List<WorkflowInstance>> getWorkflowInstancesByDocumentType(@PathVariable String documentTypeCode) {
        return ResponseEntity.ok(completeWorkflowService.getWorkflowInstancesByDocumentType(documentTypeCode));
    }
    
    /**
     * Get workflow statistics
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('CHEF_SERVICE')")
    public ResponseEntity<Object> getWorkflowStatistics() {
        return ResponseEntity.ok(completeWorkflowService.getWorkflowStatistics());
    }
    
    // ==============================================
    // WORKFLOW HISTORY
    // ==============================================
    
    /**
     * Get workflow history
     */
    @GetMapping("/history/{workflowId}")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<List<WorkflowStepExecution>> getWorkflowHistory(@PathVariable UUID workflowId) {
        return ResponseEntity.ok(workflowManagementService.getWorkflowHistory(workflowId));
    }
    
    // ==============================================
    // WORKFLOW ASSIGNMENTS
    // ==============================================
    
    /**
     * Get workflows assigned to a user
     */
    @GetMapping("/user/{userId}/workflows")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<List<WorkflowInstance>> getWorkflowsForUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(workflowManagementService.getWorkflowsForUser(userId));
    }
    
    /**
     * Get workflows for a department
     */
    @GetMapping("/department/{departmentId}/workflows")
    @PreAuthorize("hasRole('CHEF_SERVICE')")
    public ResponseEntity<List<WorkflowInstance>> getWorkflowsForDepartment(@PathVariable UUID departmentId) {
        return ResponseEntity.ok(workflowManagementService.getWorkflowsForDepartment(departmentId));
    }
    
    /**
     * Get workflows for a bureau
     */
    @GetMapping("/bureau/{bureauId}/workflows")
    @PreAuthorize("hasRole('CHEF_SERVICE')")
    public ResponseEntity<List<WorkflowInstance>> getWorkflowsForBureau(@PathVariable UUID bureauId) {
        return ResponseEntity.ok(workflowManagementService.getWorkflowsForBureau(bureauId));
    }
    
    // ==============================================
    // WORKFLOW MANAGEMENT
    // ==============================================
    
    /**
     * Create a new document type
     */
    @PostMapping("/document-types")
    @PreAuthorize("hasRole('CHEF_SERVICE')")
    public ResponseEntity<DocumentType> createDocumentType(@RequestBody DocumentType documentType) {
        return ResponseEntity.ok(workflowManagementService.createDocumentType(documentType));
    }
    
    /**
     * Create a new process step
     */
    @PostMapping("/process-steps")
    @PreAuthorize("hasRole('CHEF_SERVICE')")
    public ResponseEntity<ProcessStep> createProcessStep(@RequestBody ProcessStep processStep) {
        return ResponseEntity.ok(workflowManagementService.createProcessStep(processStep));
    }
    
    /**
     * Create a complete workflow
     */
    @PostMapping("/workflows")
    @PreAuthorize("hasRole('CHEF_SERVICE')")
    public ResponseEntity<DocumentType> createWorkflow(@RequestBody Map<String, Object> workflowData) {
        // This would create a complete workflow with document type and steps
        // Implementation depends on the request structure
        return ResponseEntity.ok().build();
    }
}


