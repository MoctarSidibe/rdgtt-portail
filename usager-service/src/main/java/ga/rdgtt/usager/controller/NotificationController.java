package ga.rdgtt.usager.controller;

import ga.rdgtt.usager.model.Notification;
import ga.rdgtt.usager.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * Obtenir les notifications d'un utilisateur
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable UUID userId) {
        try {
            List<Notification> notifications = notificationService.getNotificationsByUserId(userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Obtenir les notifications non lues d'un utilisateur
     */
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable UUID userId) {
        try {
            List<Notification> notifications = notificationService.getUnreadNotificationsByUserId(userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Marquer une notification comme lue
     */
    @PutMapping("/{notificationId}/read")
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
    @PutMapping("/user/{userId}/read-all")
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
     * Créer une nouvelle notification
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createNotification(@RequestBody Map<String, Object> notificationData) {
        try {
            UUID userId = UUID.fromString((String) notificationData.get("userId"));
            String notificationTypeCode = (String) notificationData.get("notificationTypeCode");
            String titre = (String) notificationData.get("titre");
            String message = (String) notificationData.get("message");
            
            @SuppressWarnings("unchecked")
            Map<String, Object> variables = (Map<String, Object>) notificationData.get("variables");
            
            Notification notification = notificationService.createNotification(
                userId, notificationTypeCode, titre, message, variables
            );
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "notification", notification,
                "message", "Notification créée avec succès"
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Supprimer une notification
     */
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Map<String, Object>> deleteNotification(@PathVariable UUID notificationId) {
        try {
            notificationService.deleteNotification(notificationId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Notification supprimée avec succès"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Obtenir le nombre de notifications non lues
     */
    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<Map<String, Object>> getUnreadNotificationCount(@PathVariable UUID userId) {
        try {
            List<Notification> unreadNotifications = notificationService.getUnreadNotificationsByUserId(userId);
            return ResponseEntity.ok(Map.of(
                "count", unreadNotifications.size(),
                "notifications", unreadNotifications
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Erreur lors de la récupération du nombre de notifications",
                "message", e.getMessage()
            ));
        }
    }
}
