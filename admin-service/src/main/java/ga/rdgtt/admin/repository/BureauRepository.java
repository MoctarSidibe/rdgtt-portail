package ga.rdgtt.admin.repository;

import ga.rdgtt.admin.model.Bureau;
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
    
    List<Bureau> findByDepartementId(UUID departementId);
    
    List<Bureau> findByActifTrue();
    
    @Query("SELECT b FROM Bureau b WHERE b.chefBureau.id = :chefId")
    List<Bureau> findByChefBureauId(@Param("chefId") UUID chefId);
    
    @Query("SELECT b FROM Bureau b WHERE b.departement.id = :departementId AND b.actif = true ORDER BY b.nom")
    List<Bureau> findActiveByDepartementOrderByName(@Param("departementId") UUID departementId);
    
    boolean existsByCode(String code);
}


