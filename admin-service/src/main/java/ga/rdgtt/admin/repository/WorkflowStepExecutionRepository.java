package ga.rdgtt.admin.repository;

import ga.rdgtt.admin.model.WorkflowStepExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkflowStepExecutionRepository extends JpaRepository<WorkflowStepExecution, UUID> {
    
    // Use @Query to work with the relationship
    @Query("SELECT wse FROM WorkflowStepExecution wse WHERE wse.workflowInstance.id = :workflowInstanceId ORDER BY wse.createdAt ASC")
    List<WorkflowStepExecution> findByWorkflowInstanceIdOrderByCreatedAtAsc(@Param("workflowInstanceId") UUID workflowInstanceId);
    
    @Query("SELECT wse FROM WorkflowStepExecution wse WHERE wse.workflowInstance.id = :workflowInstanceId AND wse.statut = :statut")
    List<WorkflowStepExecution> findByWorkflowInstanceIdAndStatut(@Param("workflowInstanceId") UUID workflowInstanceId, @Param("statut") String statut);
    
    List<WorkflowStepExecution> findByUtilisateurId(UUID utilisateurId);
    
    List<WorkflowStepExecution> findByDepartementId(UUID departementId);
    
    List<WorkflowStepExecution> findByBureauId(UUID bureauId);
    
    @Query("SELECT wse FROM WorkflowStepExecution wse WHERE wse.workflowInstance.id = :workflowInstanceId ORDER BY wse.dateDebut ASC")
    List<WorkflowStepExecution> findByWorkflowInstanceIdOrderByDateDebut(@Param("workflowInstanceId") UUID workflowInstanceId);
}
