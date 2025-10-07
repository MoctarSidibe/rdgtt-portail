package ga.rdgtt.usager.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_applications")
public class UserApplication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    
    @Column(name = "document_type_id")
    private UUID documentTypeId;
    
    @Column(name = "numero_demande", unique = true, nullable = false)
    private String numeroDemande;
    
    @Column(name = "statut_id")
    private UUID statutId;
    
    @Column(name = "workflow_instance_id")
    private UUID workflowInstanceId;
    
    
    @Column(name = "date_depot")
    private LocalDateTime dateDepot;
    
    @Column(name = "date_traitement")
    private LocalDateTime dateTraitement;
    
    @Column(name = "date_fin")
    private LocalDateTime dateFin;
    
    @Column(name = "delai_estime_jours")
    private Integer delaiEstimeJours;
    
    @Column(name = "commentaires", columnDefinition = "TEXT")
    private String commentaires;
    
    @Column(name = "donnees_demande", columnDefinition = "TEXT")
    private String donneesDemande;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_type_id", insertable = false, updatable = false)
    private DocumentType documentType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statut_id", insertable = false, updatable = false)
    private ApplicationStatus statut;
    
    // Constructors
    public UserApplication() {}
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    
    public UUID getDocumentTypeId() { return documentTypeId; }
    public void setDocumentTypeId(UUID documentTypeId) { this.documentTypeId = documentTypeId; }
    
    public String getNumeroDemande() { return numeroDemande; }
    public void setNumeroDemande(String numeroDemande) { this.numeroDemande = numeroDemande; }
    
    public UUID getStatutId() { return statutId; }
    public void setStatutId(UUID statutId) { this.statutId = statutId; }
    
    public UUID getWorkflowInstanceId() { return workflowInstanceId; }
    public void setWorkflowInstanceId(UUID workflowInstanceId) { this.workflowInstanceId = workflowInstanceId; }
    
    
    public LocalDateTime getDateDepot() { return dateDepot; }
    public void setDateDepot(LocalDateTime dateDepot) { this.dateDepot = dateDepot; }
    
    public LocalDateTime getDateTraitement() { return dateTraitement; }
    public void setDateTraitement(LocalDateTime dateTraitement) { this.dateTraitement = dateTraitement; }
    
    public LocalDateTime getDateFin() { return dateFin; }
    public void setDateFin(LocalDateTime dateFin) { this.dateFin = dateFin; }
    
    public Integer getDelaiEstimeJours() { return delaiEstimeJours; }
    public void setDelaiEstimeJours(Integer delaiEstimeJours) { this.delaiEstimeJours = delaiEstimeJours; }
    
    public String getCommentaires() { return commentaires; }
    public void setCommentaires(String commentaires) { this.commentaires = commentaires; }
    
    public String getDonneesDemande() { return donneesDemande; }
    public void setDonneesDemande(String donneesDemande) { this.donneesDemande = donneesDemande; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public DocumentType getDocumentType() { return documentType; }
    public void setDocumentType(DocumentType documentType) { this.documentType = documentType; }
    
    public ApplicationStatus getStatut() { return statut; }
    public void setStatut(ApplicationStatus statut) { this.statut = statut; }
}
