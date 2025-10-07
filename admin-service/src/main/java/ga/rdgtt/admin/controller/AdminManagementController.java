package ga.rdgtt.admin.controller;

import ga.rdgtt.admin.model.*;
import ga.rdgtt.admin.service.AdminUserService;
import ga.rdgtt.admin.service.DepartmentService;
import ga.rdgtt.admin.service.BureauService;
import ga.rdgtt.admin.service.WorkflowManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminManagementController {
    
    @Autowired
    private AdminUserService adminUserService;
    
    @Autowired
    private DepartmentService departmentService;
    
    @Autowired
    private BureauService bureauService;
    
    @Autowired
    private WorkflowManagementService workflowManagementService;
    
    // ==============================================
    // USER MANAGEMENT
    // ==============================================
    
    @GetMapping("/users")
    @PreAuthorize("hasRole('CHEF_SERVICE')")
    public ResponseEntity<List<AdminUser>> getAllUsers() {
        return ResponseEntity.ok(adminUserService.getAllUsers());
    }
    
    @PostMapping("/users")
    @PreAuthorize("hasRole('CHEF_SERVICE')")
    public ResponseEntity<AdminUser> createUser(@RequestBody AdminUser user) {
        return ResponseEntity.ok(adminUserService.createUser(user));
    }
    
    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('CHEF_SERVICE')")
    public ResponseEntity<AdminUser> updateUser(@PathVariable UUID id, @RequestBody AdminUser user) {
        return ResponseEntity.ok(adminUserService.updateUser(id, user));
    }
    
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('DGTT')")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        adminUserService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
    
    // ==============================================
    // DEPARTMENT MANAGEMENT
    // ==============================================
    
    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }
    
    @PostMapping("/departments")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        return ResponseEntity.ok(departmentService.createDepartment(department));
    }
    
    @PutMapping("/departments/{id}")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public ResponseEntity<Department> updateDepartment(@PathVariable UUID id, @RequestBody Department department) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, department));
    }
    
    @DeleteMapping("/departments/{id}")
    @PreAuthorize("hasRole('DGTT')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable UUID id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok().build();
    }
    
    // ==============================================
    // BUREAU MANAGEMENT
    // ==============================================
    
    @GetMapping("/bureaus")
    public ResponseEntity<List<Bureau>> getAllBureaus() {
        return ResponseEntity.ok(bureauService.getAllBureaus());
    }
    
    @GetMapping("/bureaus/department/{departmentId}")
    public ResponseEntity<List<Bureau>> getBureausByDepartment(@PathVariable UUID departmentId) {
        return ResponseEntity.ok(bureauService.getBureausByDepartment(departmentId));
    }
    
    @PostMapping("/bureaus")
    @PreAuthorize("hasRole('CHEF_SERVICE')")
    public ResponseEntity<Bureau> createBureau(@RequestBody Bureau bureau) {
        return ResponseEntity.ok(bureauService.createBureau(bureau));
    }
    
    @PutMapping("/bureaus/{id}")
    @PreAuthorize("hasRole('CHEF_SERVICE')")
    public ResponseEntity<Bureau> updateBureau(@PathVariable UUID id, @RequestBody Bureau bureau) {
        return ResponseEntity.ok(bureauService.updateBureau(id, bureau));
    }
    
    @DeleteMapping("/bureaus/{id}")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public ResponseEntity<Void> deleteBureau(@PathVariable UUID id) {
        bureauService.deleteBureau(id);
        return ResponseEntity.ok().build();
    }
    
    // ==============================================
    // WORKFLOW MANAGEMENT
    // ==============================================
    
    @GetMapping("/workflows/document-types")
    @PreAuthorize("hasRole('CHEF_SERVICE')")
    public ResponseEntity<List<DocumentType>> getAllDocumentTypes() {
        return ResponseEntity.ok(workflowManagementService.getAllDocumentTypes());
    }
    
    @PostMapping("/workflows/document-types")
    @PreAuthorize("hasRole('CHEF_SERVICE')")
    public ResponseEntity<DocumentType> createDocumentType(@RequestBody DocumentType documentType) {
        return ResponseEntity.ok(workflowManagementService.createDocumentType(documentType));
    }
    
    @GetMapping("/workflows/document-types/{id}/steps")
    @PreAuthorize("hasRole('CHEF_SERVICE')")
    public ResponseEntity<List<ProcessStep>> getProcessSteps(@PathVariable UUID id) {
        return ResponseEntity.ok(workflowManagementService.getProcessSteps(id));
    }
    
    @PostMapping("/workflows/document-types/{id}/steps")
    @PreAuthorize("hasRole('CHEF_SERVICE')")
    public ResponseEntity<ProcessStep> createProcessStep(@PathVariable UUID id, @RequestBody ProcessStep step) {
        return ResponseEntity.ok(workflowManagementService.createProcessStep(step));
    }
    
    @PostMapping("/workflows/start")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<WorkflowInstance> startWorkflow(@RequestParam String demandeId, 
                                                        @RequestParam UUID documentTypeId,
                                                        @RequestParam UUID userId) {
        AdminUser user = adminUserService.getUserById(userId);
        return ResponseEntity.ok(workflowManagementService.startWorkflow(demandeId, documentTypeId, user));
    }
    
    @PostMapping("/workflows/{workflowId}/execute")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<WorkflowStepExecution> executeStep(@PathVariable UUID workflowId,
                                                           @RequestParam UUID stepId,
                                                           @RequestParam String decision,
                                                           @RequestParam(required = false) String commentaires,
                                                           @RequestParam UUID userId) {
        AdminUser user = adminUserService.getUserById(userId);
        return ResponseEntity.ok(workflowManagementService.executeStep(workflowId, stepId, decision, commentaires, user));
    }
    
    @GetMapping("/workflows/user/{userId}")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<List<WorkflowInstance>> getWorkflowsForUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(workflowManagementService.getWorkflowsForUser(userId));
    }
    
    @GetMapping("/workflows/department/{departmentId}")
    @PreAuthorize("hasRole('CHEF_SERVICE')")
    public ResponseEntity<List<WorkflowInstance>> getWorkflowsForDepartment(@PathVariable UUID departmentId) {
        return ResponseEntity.ok(workflowManagementService.getWorkflowsForDepartment(departmentId));
    }
    
    @GetMapping("/workflows/bureau/{bureauId}")
    @PreAuthorize("hasRole('CHEF_SERVICE')")
    public ResponseEntity<List<WorkflowInstance>> getWorkflowsForBureau(@PathVariable UUID bureauId) {
        return ResponseEntity.ok(workflowManagementService.getWorkflowsForBureau(bureauId));
    }
    
    // ==============================================
    // SYSTEM STATISTICS
    // ==============================================
    
    @GetMapping("/stats")
    @PreAuthorize("hasRole('CHEF_SERVICE')")
    public ResponseEntity<Object> getSystemStats() {
        // This would return system statistics
        return ResponseEntity.ok().build();
    }
}


