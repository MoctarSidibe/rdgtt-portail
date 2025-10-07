package ga.rdgtt.admin.repository;

import ga.rdgtt.admin.model.WorkflowInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkflowInstanceRepository extends JpaRepository<WorkflowInstance, UUID> {
    
    List<WorkflowInstance> findByDemandeId(String demandeId);
    
    List<WorkflowInstance> findByStatut(String statut);
    
    @Query("SELECT w FROM WorkflowInstance w WHERE w.statut IN ('EN_ATTENTE', 'EN_COURS') AND w.utilisateurActuelId = :utilisateurId")
    List<WorkflowInstance> findPendingWorkflowsForUser(@Param("utilisateurId") UUID utilisateurId);
    
    @Query("SELECT w FROM WorkflowInstance w WHERE w.statut IN ('EN_ATTENTE', 'EN_COURS') AND w.departementActuelId = :departementId")
    List<WorkflowInstance> findPendingWorkflowsForDepartment(@Param("departementId") UUID departementId);
    
    @Query("SELECT w FROM WorkflowInstance w WHERE w.statut IN ('EN_ATTENTE', 'EN_COURS') AND w.bureauActuelId = :bureauId")
    List<WorkflowInstance> findPendingWorkflowsForBureau(@Param("bureauId") UUID bureauId);
    
    List<WorkflowInstance> findByDocumentTypeId(UUID documentTypeId);
    
    long countByStatut(String statut);
    
    List<WorkflowInstance> findByUtilisateurActuelId(UUID utilisateurId);
    
    List<WorkflowInstance> findByDepartementActuelId(UUID departementId);
    
    List<WorkflowInstance> findByBureauActuelId(UUID bureauId);
}
