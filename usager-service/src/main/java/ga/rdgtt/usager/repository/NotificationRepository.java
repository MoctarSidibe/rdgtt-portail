package ga.rdgtt.usager.repository;

import ga.rdgtt.usager.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    
    List<Notification> findByUserIdOrderByCreatedAtDesc(UUID userId);
    
    List<Notification> findByUserIdAndLuFalseOrderByCreatedAtDesc(UUID userId);
    
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.userId = :userId AND n.lu = false")
    long countByUserIdAndLuFalse(@Param("userId") UUID userId);
    
    List<Notification> findByCreatedAtBefore(LocalDateTime cutoffDate);
}
