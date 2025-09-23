package ga.rdgtt.usager.repository;

import ga.rdgtt.usager.model.UserApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserApplicationRepository extends JpaRepository<UserApplication, UUID> {
    
    List<UserApplication> findByUserIdOrderByCreatedAtDesc(UUID userId);
    
    @Query("SELECT ua FROM UserApplication ua WHERE ua.statut.code = :statusCode ORDER BY ua.createdAt DESC")
    List<UserApplication> findByStatutCodeOrderByCreatedAtDesc(@Param("statusCode") String statusCode);
    
    @Query("SELECT COUNT(ua) FROM UserApplication ua WHERE ua.statut.code = :statusCode")
    long countByStatutCode(@Param("statusCode") String statusCode);
    
    @Query("SELECT COUNT(ua) FROM UserApplication ua WHERE ua.createdAt > :date")
    long countByCreatedAtAfter(@Param("date") LocalDateTime date);
    
    @Query("SELECT ua FROM UserApplication ua WHERE ua.dateDepot < :cutoffDate AND ua.statut.code NOT IN :excludedStatuses")
    List<UserApplication> findByDateDepotBeforeAndStatutCodeNotIn(@Param("cutoffDate") LocalDateTime cutoffDate, 
                                                                 @Param("excludedStatuses") List<String> excludedStatuses);
}
