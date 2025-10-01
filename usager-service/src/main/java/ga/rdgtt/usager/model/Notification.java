package ga.rdgtt.usager.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_notifications")
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "notification_type_id")
    private UUID notificationTypeId;
    
    @Column(name = "titre", nullable = false)
    private String titre;
    
    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message;
    
    @Column(name = "lu")
    private Boolean lu = false;
    
    @Column(name = "date_lecture")
    private LocalDateTime dateLecture;
    
    @Column(name = "priorite")
    private String priorite = "NORMAL";
    
    @Column(name = "action_requise")
    private Boolean actionRequise = false;
    
    @Column(name = "action_url")
    private String actionUrl;
    
    @Column(name = "action_text")
    private String actionText;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_type_id", insertable = false, updatable = false)
    private NotificationType notificationType;
    
    // Constructors
    public Notification() {}
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public UUID getNotificationTypeId() { return notificationTypeId; }
    public void setNotificationTypeId(UUID notificationTypeId) { this.notificationTypeId = notificationTypeId; }
    
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Boolean getLu() { return lu; }
    public void setLu(Boolean lu) { this.lu = lu; }
    
    public LocalDateTime getDateLecture() { return dateLecture; }
    public void setDateLecture(LocalDateTime dateLecture) { this.dateLecture = dateLecture; }
    
    public String getPriorite() { return priorite; }
    public void setPriorite(String priorite) { this.priorite = priorite; }
    
    public Boolean getActionRequise() { return actionRequise; }
    public void setActionRequise(Boolean actionRequise) { this.actionRequise = actionRequise; }
    
    public String getActionUrl() { return actionUrl; }
    public void setActionUrl(String actionUrl) { this.actionUrl = actionUrl; }
    
    public String getActionText() { return actionText; }
    public void setActionText(String actionText) { this.actionText = actionText; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public NotificationType getNotificationType() { return notificationType; }
    public void setNotificationType(NotificationType notificationType) { this.notificationType = notificationType; }
}
