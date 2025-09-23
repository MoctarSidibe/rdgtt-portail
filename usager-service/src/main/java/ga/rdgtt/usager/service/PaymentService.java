package ga.rdgtt.usager.service;

import ga.rdgtt.usager.model.*;
import ga.rdgtt.usager.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private UserApplicationRepository userApplicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    /**
     * Créer un paiement
     */
    public Payment createPayment(UUID userId, UUID applicationId, UUID paymentMethodId, 
                               Double montant, Map<String, Object> paymentData) {
        
        // Vérifier que l'utilisateur existe
        userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        UserApplication application = userApplicationRepository.findById(applicationId)
            .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId)
            .orElseThrow(() -> new RuntimeException("Méthode de paiement non trouvée"));

        // Transaction fees calculation will be implemented when needed

        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setApplicationId(applicationId);
        payment.setDemandeId(application.getNumeroDemande());
        payment.setDocumentTypeId(application.getDocumentTypeId());
        payment.setMontant(montant);
        payment.setDevise("XAF");
        payment.setPaymentMethodId(paymentMethodId);
        payment.setStatut("EN_ATTENTE");
        payment.setReferencePaiement(generatePaymentReference());
        payment.setDateExpiration(LocalDateTime.now().plusDays(7)); // 7 jours pour payer
        payment.setDonneesPaiement(paymentData.toString()); // JSON string
        payment.setCreatedAt(LocalDateTime.now());
        
        // Log payment method info for debugging
        System.out.println("Payment created with method: " + paymentMethod.getNom());

        return paymentRepository.save(payment);
    }

    /**
     * Confirmer un paiement
     */
    public void confirmPayment(UUID paymentId, String externalReference, 
                             Map<String, Object> paymentResponse) {
        
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));

        payment.setStatut("PAYE");
        payment.setReferenceExterne(externalReference);
        payment.setDatePaiement(LocalDateTime.now());
        payment.setDonneesPaiement(paymentResponse.toString());
        payment.setUpdatedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        // Mettre à jour la demande
        UserApplication application = userApplicationRepository.findById(payment.getApplicationId())
            .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        application.setMontantPaye(application.getMontantPaye() + payment.getMontant());
        userApplicationRepository.save(application);

        // Envoyer une notification
        notificationService.notifyPaiementRecu(payment.getUserId(), 
            payment.getDemandeId(), payment.getMontant());
    }

    /**
     * Échouer un paiement
     */
    public void failPayment(UUID paymentId, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));

        payment.setStatut("ECHEC");
        payment.setUpdatedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        // Envoyer une notification d'échec
        notificationService.createNotification(payment.getUserId(), "PAIEMENT_ECHEC",
            "Paiement échoué - R-DGTT",
            "Votre paiement pour la demande {{numero_demande}} a échoué. Motif: {{motif}}",
            Map.of("numero_demande", payment.getDemandeId(), "motif", reason));
    }

    /**
     * Rembourser un paiement
     */
    public void refundPayment(UUID paymentId, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));

        payment.setStatut("REMBOURSE");
        payment.setUpdatedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        // Envoyer une notification de remboursement
        notificationService.createNotification(payment.getUserId(), "PAIEMENT_REMBOURSE",
            "Paiement remboursé - R-DGTT",
            "Votre paiement de {{montant}} XAF pour la demande {{numero_demande}} a été remboursé. Motif: {{motif}}",
            Map.of("numero_demande", payment.getDemandeId(), 
                   "montant", payment.getMontant().toString(), 
                   "motif", reason));
    }

    /**
     * Obtenir les paiements d'un utilisateur
     */
    public List<Payment> getUserPayments(UUID userId) {
        return paymentRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Obtenir les paiements en attente
     */
    public List<Payment> getPendingPayments() {
        return paymentRepository.findByStatutOrderByCreatedAtDesc("EN_ATTENTE");
    }

    /**
     * Obtenir les paiements expirés
     */
    public List<Payment> getExpiredPayments() {
        return paymentRepository.findByDateExpirationBeforeAndStatut(
            LocalDateTime.now(), "EN_ATTENTE");
    }

    /**
     * Obtenir les méthodes de paiement disponibles
     */
    public List<PaymentMethod> getAvailablePaymentMethods() {
        return paymentMethodRepository.findByActifTrueOrderByNom();
    }

    // calculateTransactionFees method removed - not currently used

    /**
     * Générer une référence de paiement unique
     */
    private String generatePaymentReference() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = String.valueOf((int) (Math.random() * 1000));
        return "PAY" + timestamp.substring(timestamp.length() - 8) + random;
    }

    /**
     * Obtenir les statistiques des paiements
     */
    public Map<String, Object> getPaymentStatistics() {
        return Map.of(
            "total_payments", paymentRepository.count(),
            "pending_payments", paymentRepository.countByStatut("EN_ATTENTE"),
            "paid_payments", paymentRepository.countByStatut("PAYE"),
            "failed_payments", paymentRepository.countByStatut("ECHEC"),
            "refunded_payments", paymentRepository.countByStatut("REMBOURSE"),
            "total_amount", paymentRepository.sumMontantByStatut("PAYE")
        );
    }

    /**
     * Nettoyer les paiements expirés
     */
    public void cleanupExpiredPayments() {
        List<Payment> expiredPayments = getExpiredPayments();
        
        for (Payment payment : expiredPayments) {
            failPayment(payment.getId(), "Paiement expiré");
        }
    }

    /**
     * Obtenir les paiements par période
     */
    public List<Payment> getPaymentsByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(startDate, endDate);
    }

    /**
     * Obtenir les paiements par méthode
     */
    public List<Payment> getPaymentsByMethod(UUID paymentMethodId) {
        return paymentRepository.findByPaymentMethodIdOrderByCreatedAtDesc(paymentMethodId);
    }

    /**
     * Vérifier si un paiement est valide
     */
    public boolean isPaymentValid(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElse(null);
        
        if (payment == null) {
            return false;
        }

        // Vérifier si le paiement n'est pas expiré
        if (payment.getDateExpiration() != null && 
            payment.getDateExpiration().isBefore(LocalDateTime.now())) {
            return false;
        }

        // Vérifier si le paiement n'est pas déjà traité
        return !payment.getStatut().equals("PAYE") && 
               !payment.getStatut().equals("ECHEC") && 
               !payment.getStatut().equals("REMBOURSE");
    }

    /**
     * Obtenir les paiements d'un utilisateur
     */
    public List<Payment> getPaymentsByUserId(UUID userId) {
        return paymentRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Obtenir un paiement par ID
     */
    public java.util.Optional<Payment> getPaymentById(UUID paymentId) {
        return paymentRepository.findById(paymentId);
    }

    /**
     * Obtenir les paiements par statut
     */
    public List<Payment> getPaymentsByStatus(String status) {
        return paymentRepository.findByStatutOrderByCreatedAtDesc(status);
    }
}
