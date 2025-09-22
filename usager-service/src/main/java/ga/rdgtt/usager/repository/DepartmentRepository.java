package ga.rdgtt.usager.repository;

import ga.rdgtt.usager.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    
    Optional<Department> findByCode(String code);
    
    List<Department> findByActifTrue();
    
    boolean existsByCode(String code);
    
    @Query("SELECT COUNT(d) FROM Department d WHERE d.actif = true")
    long countActiveDepartments();
}
