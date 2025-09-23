package ga.rdgtt.usager.controller;

import ga.rdgtt.usager.model.Payment;
import ga.rdgtt.usager.model.PaymentMethod;
import ga.rdgtt.usager.service.PaymentService;
import ga.rdgtt.usager.service.AirtelMoneySimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private AirtelMoneySimulationService airtelMoneySimulationService;

    /**
     * Créer un nouveau paiement
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createPayment(@RequestBody Map<String, Object> paymentData) {
        try {
            UUID userId = UUID.fromString((String) paymentData.get("userId"));
            UUID applicationId = UUID.fromString((String) paymentData.get("applicationId"));
            UUID paymentMethodId = UUID.fromString((String) paymentData.get("paymentMethodId"));
            Double amount = Double.valueOf(paymentData.get("amount").toString());
            
            Payment payment = paymentService.createPayment(userId, applicationId, paymentMethodId, amount, paymentData);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "payment", payment,
                "message", "Paiement créé avec succès"
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Obtenir les paiements d'un utilisateur
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payment>> getUserPayments(@PathVariable UUID userId) {
        try {
            List<Payment> payments = paymentService.getPaymentsByUserId(userId);
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Obtenir un paiement par ID
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable UUID paymentId) {
        try {
            Payment payment = paymentService.getPaymentById(paymentId)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Obtenir les méthodes de paiement disponibles
     */
    @GetMapping("/methods")
    public ResponseEntity<List<PaymentMethod>> getAvailablePaymentMethods() {
        try {
            List<PaymentMethod> methods = paymentService.getAvailablePaymentMethods();
            return ResponseEntity.ok(methods);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Simuler un paiement Airtel Money
     */
    @PostMapping("/airtel-money/simulate/{paymentId}")
    public ResponseEntity<Map<String, Object>> simulateAirtelMoneyPayment(
            @PathVariable UUID paymentId,
            @RequestBody Map<String, String> paymentData) {
        
        try {
            String phoneNumber = paymentData.get("phoneNumber");
            String pin = paymentData.get("pin");
            
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Numéro de téléphone requis"
                ));
            }
            
            if (pin == null || pin.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "PIN requis"
                ));
            }
            
            Map<String, Object> result = airtelMoneySimulationService.simulateAirtelMoneyPayment(
                paymentId, phoneNumber, pin
            );
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Obtenir les statistiques des paiements
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getPaymentStatistics() {
        try {
            Map<String, Object> statistics = paymentService.getPaymentStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Erreur lors de la récupération des statistiques",
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Obtenir les paiements par statut
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByStatus(@PathVariable String status) {
        try {
            List<Payment> payments = paymentService.getPaymentsByStatus(status);
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Obtenir les paiements expirés
     */
    @GetMapping("/expired")
    public ResponseEntity<List<Payment>> getExpiredPayments() {
        try {
            List<Payment> payments = paymentService.getExpiredPayments();
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
