package ga.rdgtt.admin.repository;

import ga.rdgtt.admin.model.ProcessStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProcessStepRepository extends JpaRepository<ProcessStep, UUID> {
    
    // Use @Query to work with the relationship
    @Query("SELECT ps FROM ProcessStep ps WHERE ps.documentType.id = :documentTypeId ORDER BY ps.ordre ASC")
    List<ProcessStep> findByDocumentTypeIdOrderByOrdreAsc(@Param("documentTypeId") UUID documentTypeId);
    
    @Query("SELECT ps FROM ProcessStep ps WHERE ps.documentType.id = :documentTypeId ORDER BY ps.ordre")
    List<ProcessStep> findByDocumentTypeIdOrderByOrdre(@Param("documentTypeId") UUID documentTypeId);
    
    List<ProcessStep> findByActifTrue();
    
    List<ProcessStep> findByDepartementId(UUID departementId);
    
    List<ProcessStep> findByBureauId(UUID bureauId);
    
    List<ProcessStep> findByRoleRequis(String roleRequis);
}