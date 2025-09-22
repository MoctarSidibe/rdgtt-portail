package ga.rdgtt.usager.repository;

import ga.rdgtt.usager.model.Bureau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BureauRepository extends JpaRepository<Bureau, UUID> {
    
    Optional<Bureau> findByCode(String code);
    
    List<Bureau> findByActifTrue();
    
    List<Bureau> findByDepartmentId(UUID departmentId);
    
    @Query("SELECT b FROM Bureau b WHERE b.actif = true AND b.department.id = :departmentId")
    List<Bureau> findActiveBureausByDepartment(@Param("departmentId") UUID departmentId);
    
    boolean existsByCode(String code);
    
    @Query("SELECT COUNT(b) FROM Bureau b WHERE b.actif = true")
    long countActiveBureaus();
    
    @Query("SELECT COUNT(b) FROM Bureau b WHERE b.actif = true AND b.department.id = :departmentId")
    long countActiveBureausByDepartment(@Param("departmentId") UUID departmentId);
}
