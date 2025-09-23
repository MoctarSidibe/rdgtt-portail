package ga.rdgtt.usager.controller;

import ga.rdgtt.usager.model.*;
import ga.rdgtt.usager.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-interface")
@CrossOrigin(origins = "*")
public class UserInterfaceController {

    @Autowired
    private ApplicationService applicationService;
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private AirtelMoneySimulationService airtelMoneySimulationService;

    /**
     * Obtenir le tableau de bord d'un utilisateur
     */
    @GetMapping("/dashboard/{userId}")
    public ResponseEntity<Map<String, Object>> getUserDashboard(@PathVariable UUID userId) {
        try {
            // Récupérer les applications de l'utilisateur
            List<UserApplication> applications = applicationService.getUserApplications(userId);
            
            // Récupérer les notifications non lues
            List<Notification> notifications = notificationService.getUnreadNotificationsByUserId(userId);
            
            // Récupérer les paiements récents
            List<Payment> payments = paymentService.getPaymentsByUserId(userId);
            
            // Statistiques
            Map<String, Long> applicationStats = applicationService.getApplicationStatistics();
            
            return ResponseEntity.ok(Map.of(
                "applications", applications,
                "notifications", notifications,
                "payments", payments,
                "statistics", applicationStats,
                "unread_notifications_count", notifications.size()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Erreur lors de la récupération du tableau de bord",
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Obtenir les applications d'un utilisateur
     */
    @GetMapping("/applications/{userId}")
    public ResponseEntity<List<UserApplication>> getUserApplications(@PathVariable UUID userId) {
        try {
            List<UserApplication> applications = applicationService.getUserApplications(userId);
            return ResponseEntity.ok(applications);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Obtenir les notifications d'un utilisateur
     */
    @GetMapping("/notifications/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable UUID userId) {
        try {
            List<Notification> notifications = notificationService.getNotificationsByUserId(userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Marquer une notification comme lue
     */
    @PutMapping("/notifications/{notificationId}/read")
    public ResponseEntity<Map<String, Object>> markNotificationAsRead(@PathVariable UUID notificationId) {
        try {
            notificationService.markNotificationAsRead(notificationId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Notification marquée comme lue"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Marquer toutes les notifications d'un utilisateur comme lues
     */
    @PutMapping("/notifications/{userId}/read-all")
    public ResponseEntity<Map<String, Object>> markAllNotificationsAsRead(@PathVariable UUID userId) {
        try {
            notificationService.markAllAsRead(userId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Toutes les notifications ont été marquées comme lues"
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
    @GetMapping("/payments/{userId}")
    public ResponseEntity<List<Payment>> getUserPayments(@PathVariable UUID userId) {
        try {
            List<Payment> payments = paymentService.getPaymentsByUserId(userId);
            return ResponseEntity.ok(payments);
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
     * Obtenir les méthodes de paiement disponibles
     */
    @GetMapping("/payment-methods")
    public ResponseEntity<List<PaymentMethod>> getAvailablePaymentMethods() {
        try {
            List<PaymentMethod> methods = paymentService.getAvailablePaymentMethods();
            return ResponseEntity.ok(methods);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Créer un nouveau paiement
     */
    @PostMapping("/payments/create")
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
     * Obtenir les statistiques d'un utilisateur
     */
    @GetMapping("/statistics/{userId}")
    public ResponseEntity<Map<String, Object>> getUserStatistics(@PathVariable UUID userId) {
        try {
            List<UserApplication> applications = applicationService.getUserApplications(userId);
            List<Payment> payments = paymentService.getPaymentsByUserId(userId);
            List<Notification> notifications = notificationService.getNotificationsByUserId(userId);
            
            long totalApplications = applications.size();
            long pendingApplications = applications.stream()
                .filter(app -> "DEPOSEE".equals(app.getStatut().getCode()) || "EN_COURS".equals(app.getStatut().getCode()))
                .count();
            long completedApplications = applications.stream()
                .filter(app -> "TERMINEE".equals(app.getStatut().getCode()))
                .count();
            
            long totalPayments = payments.size();
            long successfulPayments = payments.stream()
                .filter(payment -> "VALIDE".equals(payment.getStatut()))
                .count();
            
            long unreadNotifications = notifications.stream()
                .filter(notification -> !notification.getLu())
                .count();
            
            return ResponseEntity.ok(Map.of(
                "applications", Map.of(
                    "total", totalApplications,
                    "pending", pendingApplications,
                    "completed", completedApplications
                ),
                "payments", Map.of(
                    "total", totalPayments,
                    "successful", successfulPayments
                ),
                "notifications", Map.of(
                    "total", notifications.size(),
                    "unread", unreadNotifications
                )
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Erreur lors de la récupération des statistiques",
                "message", e.getMessage()
            ));
        }
    }
}
