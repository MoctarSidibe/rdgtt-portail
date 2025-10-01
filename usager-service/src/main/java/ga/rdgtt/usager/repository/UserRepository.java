package ga.rdgtt.usager.repository;

import ga.rdgtt.usager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    List<User> findByActifTrue();
    
    List<User> findByRoleCode(String roleCode);
    
    List<User> findByDepartmentId(UUID departmentId);
    
    List<User> findByBureauId(UUID bureauId);
    
    @Query("SELECT u FROM User u WHERE u.actif = true AND u.role.code = :roleCode")
    List<User> findActiveUsersByRole(@Param("roleCode") String roleCode);
    
    @Query("SELECT u FROM User u WHERE u.actif = true AND u.department.id = :departmentId")
    List<User> findActiveUsersByDepartment(@Param("departmentId") UUID departmentId);
    
    @Query("SELECT u FROM User u WHERE u.actif = true AND u.bureau.id = :bureauId")
    List<User> findActiveUsersByBureau(@Param("bureauId") UUID bureauId);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.actif = true")
    long countActiveUsers();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.actif = true AND u.role.code = :roleCode")
    long countActiveUsersByRole(@Param("roleCode") String roleCode);
}
