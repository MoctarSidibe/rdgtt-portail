package ga.rdgtt.admin.repository;

import ga.rdgtt.admin.model.WorkflowStepExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkflowStepExecutionRepository extends JpaRepository<WorkflowStepExecution, UUID> {
    
    // Use the relationship instead of direct field
    List<WorkflowStepExecution> findByWorkflowInstanceIdOrderByCreatedAtAsc(UUID workflowInstanceId);
    
    List<WorkflowStepExecution> findByWorkflowInstanceIdAndStatut(UUID workflowInstanceId, String statut);
    
    List<WorkflowStepExecution> findByUtilisateurId(UUID utilisateurId);
    
    List<WorkflowStepExecution> findByDepartementId(UUID departementId);
    
    List<WorkflowStepExecution> findByBureauId(UUID bureauId);
    
    List<WorkflowStepExecution> findByWorkflowInstanceIdOrderByDateDebut(UUID workflowInstanceId);
}
