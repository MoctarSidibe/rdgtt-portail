package ga.rdgtt.admin.repository;

import ga.rdgtt.admin.model.AdminUser;
import ga.rdgtt.admin.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, UUID> {
    
    Optional<AdminUser> findByEmail(String email);
    
    List<AdminUser> findByRole(UserRole role);
    
    List<AdminUser> findByDepartementId(UUID departementId);
    
    List<AdminUser> findByBureauId(UUID bureauId);
    
    List<AdminUser> findByActifTrue();
    
    @Query("SELECT u FROM AdminUser u WHERE u.role IN :roles")
    List<AdminUser> findByRoleIn(@Param("roles") List<UserRole> roles);
    
    @Query("SELECT u FROM AdminUser u WHERE u.departement.id = :departementId AND u.actif = true")
    List<AdminUser> findActiveUsersByDepartement(@Param("departementId") UUID departementId);
    
    @Query("SELECT u FROM AdminUser u WHERE u.bureau.id = :bureauId AND u.actif = true")
    List<AdminUser> findActiveUsersByBureau(@Param("bureauId") UUID bureauId);
    
    @Query("SELECT u FROM AdminUser u WHERE u.role = :role AND u.actif = true")
    List<AdminUser> findActiveUsersByRole(@Param("role") UserRole role);
    
    boolean existsByEmail(String email);
}


