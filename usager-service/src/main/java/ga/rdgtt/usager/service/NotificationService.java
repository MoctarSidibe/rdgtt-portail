package ga.rdgtt.usager.service;

import ga.rdgtt.usager.model.Notification;
import ga.rdgtt.usager.model.NotificationType;
import ga.rdgtt.usager.model.User;
import ga.rdgtt.usager.repository.NotificationRepository;
import ga.rdgtt.usager.repository.NotificationTypeRepository;
import ga.rdgtt.usager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationTypeRepository notificationTypeRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Créer une notification pour un utilisateur
     */
    public Notification createNotification(UUID userId, String notificationTypeCode, 
                                         String titre, String message, 
                                         Map<String, Object> variables) {
        
        // Vérifier que l'utilisateur existe
        userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        NotificationType notificationType = notificationTypeRepository.findByCode(notificationTypeCode)
            .orElseThrow(() -> new RuntimeException("Type de notification non trouvé"));

        // Remplacer les variables dans le message
        String processedMessage = processMessageTemplate(message, variables);
        String processedTitre = processMessageTemplate(titre, variables);

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setNotificationTypeId(notificationType.getId());
        notification.setTitre(processedTitre);
        notification.setMessage(processedMessage);
        notification.setLu(false);
        notification.setPriorite("NORMAL"); // Default priority
        notification.setActionRequise(false); // Default action required
        notification.setActionUrl(null);
        notification.setActionText(null);
        notification.setCreatedAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    /**
     * Marquer une notification comme lue
     */
    public void markAsRead(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("Notification non trouvée"));
        
        notification.setLu(true);
        notification.setDateLecture(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    /**
     * Marquer toutes les notifications d'un utilisateur comme lues
     */
    public void markAllAsRead(UUID userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndLuFalseOrderByCreatedAtDesc(userId);
        notifications.forEach(notification -> {
            notification.setLu(true);
            notification.setDateLecture(LocalDateTime.now());
        });
        notificationRepository.saveAll(notifications);
    }

    /**
     * Obtenir les notifications non lues d'un utilisateur
     */
    public List<Notification> getUnreadNotifications(UUID userId) {
        return notificationRepository.findByUserIdAndLuFalseOrderByCreatedAtDesc(userId);
    }

    /**
     * Obtenir toutes les notifications d'un utilisateur
     */
    public List<Notification> getAllNotifications(UUID userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Supprimer les notifications anciennes (plus de 30 jours)
     */
    public void cleanupOldNotifications() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);
        List<Notification> oldNotifications = notificationRepository.findByCreatedAtBefore(cutoffDate);
        notificationRepository.deleteAll(oldNotifications);
    }

    /**
     * Notifications spécifiques pour les demandes
     */
    public void notifyDemandeDeposee(UUID userId, String numeroDemande, String typeDocument) {
        createNotification(userId, "DEMANDE_DEPOSEE", 
            "Demande déposée - R-DGTT",
            "Votre demande {{numero_demande}} pour {{type_document}} a été déposée avec succès.",
            Map.of("numero_demande", numeroDemande, "type_document", typeDocument));
    }

    public void notifyStatutChange(UUID userId, String numeroDemande, String ancienStatut, String nouveauStatut) {
        createNotification(userId, "STATUT_CHANGE",
            "Statut mis à jour - R-DGTT",
            "Le statut de votre demande {{numero_demande}} est passé de {{ancien_statut}} à {{nouveau_statut}}.",
            Map.of("numero_demande", numeroDemande, 
                   "ancien_statut", ancienStatut, 
                   "nouveau_statut", nouveauStatut));
    }

    public void notifyDocumentValide(UUID userId, String numeroDemande, String nomDocument) {
        createNotification(userId, "DOCUMENT_VALIDE",
            "Document validé - R-DGTT",
            "Votre document {{nom_document}} pour la demande {{numero_demande}} a été validé.",
            Map.of("numero_demande", numeroDemande, "nom_document", nomDocument));
    }

    public void notifyDocumentRejete(UUID userId, String numeroDemande, String nomDocument, String motif) {
        createNotification(userId, "DOCUMENT_REJETE",
            "Document rejeté - R-DGTT",
            "Votre document {{nom_document}} pour la demande {{numero_demande}} a été rejeté. Motif: {{motif}}",
            Map.of("numero_demande", numeroDemande, 
                   "nom_document", nomDocument, 
                   "motif", motif));
    }


    public void notifyRappelDocument(UUID userId, String numeroDemande, String documentsManquants) {
        createNotification(userId, "RAPPEL_DOCUMENT",
            "Document manquant - R-DGTT",
            "Il manque des documents pour votre demande {{numero_demande}}: {{documents_manquants}}",
            Map.of("numero_demande", numeroDemande, "documents_manquants", documentsManquants));
    }

    /**
     * Traiter le template de message avec les variables
     */
    private String processMessageTemplate(String template, Map<String, Object> variables) {
        String result = template;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            result = result.replace(placeholder, entry.getValue().toString());
        }
        return result;
    }

    /**
     * Obtenir le nombre de notifications non lues
     */
    public long getUnreadCount(UUID userId) {
        return notificationRepository.countByUserIdAndLuFalse(userId);
    }

    /**
     * Notifier tous les utilisateurs d'un département
     */
    public void notifyDepartment(UUID departmentId, String notificationTypeCode, 
                                String titre, String message, Map<String, Object> variables) {
        List<User> users = userRepository.findByDepartmentId(departmentId);
        for (User user : users) {
            createNotification(user.getId(), notificationTypeCode, titre, message, variables);
        }
    }

    /**
     * Notifier tous les utilisateurs d'un bureau
     */
    public void notifyBureau(UUID bureauId, String notificationTypeCode, 
                            String titre, String message, Map<String, Object> variables) {
        List<User> users = userRepository.findByBureauId(bureauId);
        for (User user : users) {
            createNotification(user.getId(), notificationTypeCode, titre, message, variables);
        }
    }

    /**
     * Obtenir les notifications d'un utilisateur (alias pour getAllNotifications)
     */
    public List<Notification> getNotificationsByUserId(UUID userId) {
        return getAllNotifications(userId);
    }

    /**
     * Obtenir les notifications non lues d'un utilisateur (alias pour getUnreadNotifications)
     */
    public List<Notification> getUnreadNotificationsByUserId(UUID userId) {
        return getUnreadNotifications(userId);
    }

    /**
     * Marquer une notification comme lue (alias pour markAsRead)
     */
    public void markNotificationAsRead(UUID notificationId) {
        markAsRead(notificationId);
    }

    /**
     * Supprimer une notification
     */
    public void deleteNotification(UUID notificationId) {
        notificationRepository.deleteById(notificationId);
    }
}
