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
            
            
            // Statistiques
            Map<String, Long> applicationStats = applicationService.getApplicationStatistics();
            
            return ResponseEntity.ok(Map.of(
                "applications", applications,
                "notifications", notifications,
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
     * Obtenir les statistiques d'un utilisateur
     */
    @GetMapping("/statistics/{userId}")
    public ResponseEntity<Map<String, Object>> getUserStatistics(@PathVariable UUID userId) {
        try {
            List<UserApplication> applications = applicationService.getUserApplications(userId);
            List<Notification> notifications = notificationService.getNotificationsByUserId(userId);
            
            long totalApplications = applications.size();
            long pendingApplications = applications.stream()
                .filter(app -> "DEPOSEE".equals(app.getStatut().getCode()) || "EN_COURS".equals(app.getStatut().getCode()))
                .count();
            long completedApplications = applications.stream()
                .filter(app -> "TERMINEE".equals(app.getStatut().getCode()))
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
