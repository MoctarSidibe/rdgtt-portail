package ga.rdgtt.usager.repository;

import ga.rdgtt.usager.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, UUID> {
    
    Optional<PaymentMethod> findByCode(String code);
    
    List<PaymentMethod> findByActifTrueOrderByNom();
}
