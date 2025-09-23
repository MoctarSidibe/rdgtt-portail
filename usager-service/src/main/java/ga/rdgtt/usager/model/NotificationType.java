package ga.rdgtt.usager.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notification_types")
public class NotificationType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(name = "code", unique = true, nullable = false)
    private String code;
    
    @Column(name = "nom", nullable = false)
    private String nom;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "template_subject", columnDefinition = "TEXT")
    private String templateSubject;
    
    @Column(name = "template_body", columnDefinition = "TEXT")
    private String templateBody;
    
    @Column(name = "actif")
    private Boolean actif = true;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Constructors
    public NotificationType() {}
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getTemplateSubject() { return templateSubject; }
    public void setTemplateSubject(String templateSubject) { this.templateSubject = templateSubject; }
    
    public String getTemplateBody() { return templateBody; }
    public void setTemplateBody(String templateBody) { this.templateBody = templateBody; }
    
    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
