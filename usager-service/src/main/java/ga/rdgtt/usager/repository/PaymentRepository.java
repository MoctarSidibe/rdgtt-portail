package ga.rdgtt.usager.repository;

import ga.rdgtt.usager.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    
    List<Payment> findByUserIdOrderByCreatedAtDesc(UUID userId);
    
    List<Payment> findByStatutOrderByCreatedAtDesc(String statut);
    
    List<Payment> findByDateExpirationBeforeAndStatut(LocalDateTime date, String statut);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.statut = :statut")
    long countByStatut(@Param("statut") String statut);
    
    @Query("SELECT SUM(p.montant) FROM Payment p WHERE p.statut = :statut")
    Double sumMontantByStatut(@Param("statut") String statut);
    
    List<Payment> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Payment> findByPaymentMethodIdOrderByCreatedAtDesc(UUID paymentMethodId);
}
