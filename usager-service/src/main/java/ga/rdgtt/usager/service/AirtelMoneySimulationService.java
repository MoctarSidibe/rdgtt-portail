package ga.rdgtt.usager.service;

import ga.rdgtt.usager.model.Payment;
import ga.rdgtt.usager.model.PaymentMethod;
import ga.rdgtt.usager.repository.PaymentRepository;
import ga.rdgtt.usager.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.Random;

@Service
@Transactional
public class AirtelMoneySimulationService {

    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    
    @Autowired
    private NotificationService notificationService;

    /**
     * Simuler un paiement Airtel Money
     */
    public Map<String, Object> simulateAirtelMoneyPayment(UUID paymentId, String phoneNumber, String pin) {
        
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));
        
        PaymentMethod airtelMethod = paymentMethodRepository.findByCode("MOBILE_MONEY")
            .orElseThrow(() -> new RuntimeException("Méthode Airtel Money non trouvée"));
        
        // Vérifier que c'est bien un paiement Airtel Money
        if (!airtelMethod.getId().equals(payment.getPaymentMethodId())) {
            throw new RuntimeException("Ce paiement n'est pas configuré pour Airtel Money");
        }
        
        // Simuler la validation du numéro de téléphone (format gabonais)
        if (!isValidGabonesePhoneNumber(phoneNumber)) {
            return Map.of(
                "success", false,
                "message", "Numéro de téléphone invalide. Format attendu: +241XXXXXXXX",
                "error_code", "INVALID_PHONE"
            );
        }
        
        // Simuler la validation du PIN (4 chiffres)
        if (!isValidPin(pin)) {
            return Map.of(
                "success", false,
                "message", "PIN invalide. Le PIN doit contenir 4 chiffres",
                "error_code", "INVALID_PIN"
            );
        }
        
        // Simuler les différents scénarios de paiement
        PaymentSimulationResult result = simulatePaymentScenario(payment, phoneNumber);
        
        if (result.isSuccess()) {
            // Mettre à jour le paiement
            payment.setStatut("VALIDE");
            payment.setReferenceExterne(result.getExternalReference());
            payment.setDatePaiement(LocalDateTime.now());
            payment.setDonneesPaiement(String.format(
                "{\"phone\":\"%s\",\"provider\":\"AIRTEL\",\"transaction_id\":\"%s\"}", 
                phoneNumber, result.getExternalReference()
            ));
            
            paymentRepository.save(payment);
            
            // Envoyer une notification de succès
            notificationService.createNotification(
                payment.getUserId(),
                "PAIEMENT_RECU",
                "Paiement reçu",
                "Votre paiement de {{montant}} {{devise}} a été effectué avec succès via {{methode}}. Référence: {{reference}}",
                Map.of(
                    "montant", payment.getMontant().toString(),
                    "devise", payment.getDevise(),
                    "reference", payment.getReferencePaiement(),
                    "methode", "Airtel Money"
                )
            );
            
            return Map.of(
                "success", true,
                "message", "Paiement effectué avec succès",
                "transaction_id", result.getExternalReference(),
                "amount", payment.getMontant(),
                "currency", payment.getDevise(),
                "phone", phoneNumber
            );
        } else {
            // Mettre à jour le paiement en échec
            payment.setStatut("ECHEC");
            payment.setReferenceExterne(result.getExternalReference());
            payment.setDatePaiement(LocalDateTime.now());
            payment.setDonneesPaiement(String.format(
                "{\"phone\":\"%s\",\"provider\":\"AIRTEL\",\"error\":\"%s\"}", 
                phoneNumber, result.getErrorMessage()
            ));
            
            paymentRepository.save(payment);
            
            return Map.of(
                "success", false,
                "message", result.getErrorMessage(),
                "error_code", result.getErrorCode(),
                "transaction_id", result.getExternalReference()
            );
        }
    }
    
    /**
     * Simuler différents scénarios de paiement
     */
    private PaymentSimulationResult simulatePaymentScenario(Payment payment, String phoneNumber) {
        Random random = new Random();
        int scenario = random.nextInt(100);
        
        // 85% de succès, 15% d'échec
        if (scenario < 85) {
            return new PaymentSimulationResult(
                true,
                null,
                null,
                generateAirtelTransactionId(),
                null
            );
        } else {
            // Simuler différents types d'erreurs
            String[] errorMessages = {
                "Solde insuffisant sur le compte Airtel Money",
                "Numéro de téléphone non enregistré sur Airtel Money",
                "Service temporairement indisponible",
                "PIN incorrect",
                "Limite de transaction dépassée"
            };
            
            String[] errorCodes = {
                "INSUFFICIENT_BALANCE",
                "UNREGISTERED_PHONE",
                "SERVICE_UNAVAILABLE",
                "INVALID_PIN",
                "LIMIT_EXCEEDED"
            };
            
            int errorIndex = random.nextInt(errorMessages.length);
            
            return new PaymentSimulationResult(
                false,
                errorMessages[errorIndex],
                errorCodes[errorIndex],
                generateAirtelTransactionId(),
                null
            );
        }
    }
    
    /**
     * Valider le format du numéro de téléphone gabonais
     */
    private boolean isValidGabonesePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        
        // Format gabonais: +241XXXXXXXX (9 chiffres après +241)
        String cleanNumber = phoneNumber.replaceAll("\\s+", "");
        return cleanNumber.matches("^\\+241[0-9]{9}$");
    }
    
    /**
     * Valider le format du PIN
     */
    private boolean isValidPin(String pin) {
        return pin != null && pin.matches("^[0-9]{4}$");
    }
    
    /**
     * Générer un ID de transaction Airtel simulé
     */
    private String generateAirtelTransactionId() {
        Random random = new Random();
        long timestamp = System.currentTimeMillis();
        int randomPart = random.nextInt(10000);
        return String.format("AIRTEL_%d_%04d", timestamp, randomPart);
    }
    
    /**
     * Classe interne pour représenter le résultat de la simulation
     */
    private static class PaymentSimulationResult {
        private final boolean success;
        private final String errorMessage;
        private final String errorCode;
        private final String externalReference;
        // private final String additionalData; // Not used currently
        
        public PaymentSimulationResult(boolean success, String errorMessage, String errorCode, 
                                     String externalReference, String additionalData) {
            this.success = success;
            this.errorMessage = errorMessage;
            this.errorCode = errorCode;
            this.externalReference = externalReference;
            // this.additionalData = additionalData; // Not used currently
        }
        
        public boolean isSuccess() { return success; }
        public String getErrorMessage() { return errorMessage; }
        public String getErrorCode() { return errorCode; }
        public String getExternalReference() { return externalReference; }
        // public String getAdditionalData() { return additionalData; } // Not used currently
    }
}
