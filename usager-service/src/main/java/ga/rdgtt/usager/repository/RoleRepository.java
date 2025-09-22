package ga.rdgtt.usager.repository;

import ga.rdgtt.usager.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    
    Optional<Role> findByCode(String code);
    
    boolean existsByCode(String code);
}
