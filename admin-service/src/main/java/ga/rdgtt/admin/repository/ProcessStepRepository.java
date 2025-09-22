package ga.rdgtt.admin.repository;

import ga.rdgtt.admin.model.ProcessStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProcessStepRepository extends JpaRepository<ProcessStep, UUID> {
    
    List<ProcessStep> findByDocumentTypeIdOrderByOrdreAsc(UUID documentTypeId);
    
    List<ProcessStep> findByDocumentTypeIdAndActifTrueOrderByOrdreAsc(UUID documentTypeId);
    
    List<ProcessStep> findByActifTrue();
}
