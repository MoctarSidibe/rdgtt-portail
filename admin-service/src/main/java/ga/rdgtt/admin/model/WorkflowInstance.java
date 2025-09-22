package ga.rdgtt.admin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "workflow_instances")
public class WorkflowInstance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotBlank(message = "L'ID de la demande est obligatoire")
    @Column(name = "demande_id", nullable = false)
    private String demandeId; // Reference to the main request (permis, carte-grise, etc.)
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_type_id", nullable = false)
    private DocumentType documentType;
    
    @Column(name = "statut", nullable = false)
    private String statut; // EN_ATTENTE, EN_COURS, VALIDE, REJETE, ANNULE
    
    @Column(name = "etape_actuelle_id")
    private UUID etapeActuelleId;
    
    @Column(name = "utilisateur_actuel_id")
    private UUID utilisateurActuelId;
    
    @Column(name = "departement_actuel_id")
    private UUID departementActuelId;
    
    @Column(name = "bureau_actuel_id")
    private UUID bureauActuelId;
    
    @Column(name = "date_debut")
    private LocalDateTime dateDebut;
    
    @Column(name = "date_fin")
    private LocalDateTime dateFin;
    
    @Column(name = "delai_max_jours")
    private Integer delaiMaxJours;
    
    @Column(name = "commentaires", columnDefinition = "TEXT")
    private String commentaires;
    
    @Column(name = "donnees_contexte", columnDefinition = "TEXT")
    private String donneesContexte; // JSON context data
    
    @OneToMany(mappedBy = "workflowInstance", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkflowStepExecution> stepExecutions;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public WorkflowInstance() {}
    
    public WorkflowInstance(String demandeId, DocumentType documentType) {
        this.demandeId = demandeId;
        this.documentType = documentType;
        this.statut = "EN_ATTENTE";
        this.dateDebut = LocalDateTime.now();
    }
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getDemandeId() { return demandeId; }
    public void setDemandeId(String demandeId) { this.demandeId = demandeId; }
    
    public DocumentType getDocumentType() { return documentType; }
    public void setDocumentType(DocumentType documentType) { this.documentType = documentType; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    public UUID getEtapeActuelleId() { return etapeActuelleId; }
    public void setEtapeActuelleId(UUID etapeActuelleId) { this.etapeActuelleId = etapeActuelleId; }
    
    public UUID getUtilisateurActuelId() { return utilisateurActuelId; }
    public void setUtilisateurActuelId(UUID utilisateurActuelId) { this.utilisateurActuelId = utilisateurActuelId; }
    
    public UUID getDepartementActuelId() { return departementActuelId; }
    public void setDepartementActuelId(UUID departementActuelId) { this.departementActuelId = departementActuelId; }
    
    public UUID getBureauActuelId() { return bureauActuelId; }
    public void setBureauActuelId(UUID bureauActuelId) { this.bureauActuelId = bureauActuelId; }
    
    public LocalDateTime getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDateTime dateDebut) { this.dateDebut = dateDebut; }
    
    public LocalDateTime getDateFin() { return dateFin; }
    public void setDateFin(LocalDateTime dateFin) { this.dateFin = dateFin; }
    
    public Integer getDelaiMaxJours() { return delaiMaxJours; }
    public void setDelaiMaxJours(Integer delaiMaxJours) { this.delaiMaxJours = delaiMaxJours; }
    
    public String getCommentaires() { return commentaires; }
    public void setCommentaires(String commentaires) { this.commentaires = commentaires; }
    
    public String getDonneesContexte() { return donneesContexte; }
    public void setDonneesContexte(String donneesContexte) { this.donneesContexte = donneesContexte; }
    
    public List<WorkflowStepExecution> getStepExecutions() { return stepExecutions; }
    public void setStepExecutions(List<WorkflowStepExecution> stepExecutions) { this.stepExecutions = stepExecutions; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
