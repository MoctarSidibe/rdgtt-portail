package ga.rdgtt.admin.repository;

import ga.rdgtt.admin.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    
    Optional<Department> findByCode(String code);
    
    List<Department> findByActifTrue();
    
    @Query("SELECT d FROM Department d WHERE d.chefDepartement.id = :chefId")
    List<Department> findByChefDepartementId(@Param("chefId") UUID chefId);
    
    @Query("SELECT d FROM Department d WHERE d.actif = true ORDER BY d.nom")
    List<Department> findAllActiveOrderByName();
    
    boolean existsByCode(String code);
}


