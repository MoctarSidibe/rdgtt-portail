package ga.rdgtt.admin.repository;

import ga.rdgtt.admin.model.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, UUID> {
    
    List<DocumentType> findByServiceCode(String serviceCode);
    
    List<DocumentType> findByActifTrue();
    
    List<DocumentType> findByServiceCodeAndActifTrue(String serviceCode);
}
