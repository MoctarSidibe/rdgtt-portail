package ga.rdgtt.usager.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "application_history")
public class ApplicationHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(name = "application_id", nullable = false)
    private UUID applicationId;
    
    @Column(name = "ancien_statut_id")
    private UUID ancienStatutId;
    
    @Column(name = "nouveau_statut_id")
    private UUID nouveauStatutId;
    
    @Column(name = "commentaire", columnDefinition = "TEXT")
    private String commentaire;
    
    @Column(name = "utilisateur_id")
    private UUID utilisateurId;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", insertable = false, updatable = false)
    private UserApplication application;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ancien_statut_id", insertable = false, updatable = false)
    private ApplicationStatus ancienStatut;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nouveau_statut_id", insertable = false, updatable = false)
    private ApplicationStatus nouveauStatut;
    
    // Constructors
    public ApplicationHistory() {}
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public UUID getApplicationId() { return applicationId; }
    public void setApplicationId(UUID applicationId) { this.applicationId = applicationId; }
    
    public UUID getAncienStatutId() { return ancienStatutId; }
    public void setAncienStatutId(UUID ancienStatutId) { this.ancienStatutId = ancienStatutId; }
    
    public UUID getNouveauStatutId() { return nouveauStatutId; }
    public void setNouveauStatutId(UUID nouveauStatutId) { this.nouveauStatutId = nouveauStatutId; }
    
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
    
    public UUID getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(UUID utilisateurId) { this.utilisateurId = utilisateurId; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public UserApplication getApplication() { return application; }
    public void setApplication(UserApplication application) { this.application = application; }
    
    public ApplicationStatus getAncienStatut() { return ancienStatut; }
    public void setAncienStatut(ApplicationStatus ancienStatut) { this.ancienStatut = ancienStatut; }
    
    public ApplicationStatus getNouveauStatut() { return nouveauStatut; }
    public void setNouveauStatut(ApplicationStatus nouveauStatut) { this.nouveauStatut = nouveauStatut; }
}
