package ga.rdgtt.admin.service;

import ga.rdgtt.admin.model.WorkflowInstance;
import ga.rdgtt.admin.repository.WorkflowInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WorkflowService {

    @Autowired
    private WorkflowInstanceRepository workflowInstanceRepository;

    public List<WorkflowInstance> getAllWorkflows() {
        return workflowInstanceRepository.findAll();
    }

    public Optional<WorkflowInstance> getWorkflowById(UUID id) {
        return workflowInstanceRepository.findById(id);
    }

    public Optional<WorkflowInstance> getWorkflowByDemandeId(String demandeId) {
        return workflowInstanceRepository.findByDemandeId(demandeId);
    }

    public List<WorkflowInstance> getWorkflowsByStatus(String status) {
        return workflowInstanceRepository.findByStatut(status);
    }

    public List<WorkflowInstance> getWorkflowsByUserId(UUID userId) {
        return workflowInstanceRepository.findByUtilisateurActuelId(userId);
    }

    public WorkflowInstance saveWorkflow(WorkflowInstance workflow) {
        return workflowInstanceRepository.save(workflow);
    }

    public void deleteWorkflow(UUID id) {
        workflowInstanceRepository.deleteById(id);
    }

    public long countByStatus(String status) {
        return workflowInstanceRepository.countByStatut(status);
    }

    public List<WorkflowInstance> getWorkflowsByDocumentType(UUID documentTypeId) {
        return workflowInstanceRepository.findByDocumentTypeId(documentTypeId);
    }
}
