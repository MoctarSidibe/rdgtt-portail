package ga.rdgtt.admin.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "workflow_step_executions")
public class WorkflowStepExecution {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_instance_id", nullable = false)
    private WorkflowInstance workflowInstance;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_step_id", nullable = false)
    private ProcessStep processStep;
    
    @Column(name = "statut", nullable = false)
    private String statut; // EN_ATTENTE, EN_COURS, VALIDE, REJETE, IGNORE
    
    @Column(name = "utilisateur_id")
    private UUID utilisateurId;
    
    @Column(name = "departement_id")
    private UUID departementId;
    
    @Column(name = "bureau_id")
    private UUID bureauId;
    
    @Column(name = "date_debut")
    private LocalDateTime dateDebut;
    
    @Column(name = "date_fin")
    private LocalDateTime dateFin;
    
    @Column(name = "commentaires", columnDefinition = "TEXT")
    private String commentaires;
    
    @Column(name = "donnees_validation", columnDefinition = "TEXT")
    private String donneesValidation; // JSON validation data
    
    @Column(name = "decision")
    private String decision; // VALIDE, REJETE, REDIRIGE
    
    @Column(name = "etape_suivante_id")
    private UUID etapeSuivanteId;
    
    @Column(name = "raison_rejet")
    private String raisonRejet;
    
    @Column(name = "delai_depasse")
    private Boolean delaiDepasse = false;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public WorkflowStepExecution() {}
    
    public WorkflowStepExecution(WorkflowInstance workflowInstance, ProcessStep processStep) {
        this.workflowInstance = workflowInstance;
        this.processStep = processStep;
        this.statut = "EN_ATTENTE";
    }
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public WorkflowInstance getWorkflowInstance() { return workflowInstance; }
    public void setWorkflowInstance(WorkflowInstance workflowInstance) { this.workflowInstance = workflowInstance; }
    
    public ProcessStep getProcessStep() { return processStep; }
    public void setProcessStep(ProcessStep processStep) { this.processStep = processStep; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    public UUID getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(UUID utilisateurId) { this.utilisateurId = utilisateurId; }
    
    public UUID getDepartementId() { return departementId; }
    public void setDepartementId(UUID departementId) { this.departementId = departementId; }
    
    public UUID getBureauId() { return bureauId; }
    public void setBureauId(UUID bureauId) { this.bureauId = bureauId; }
    
    public LocalDateTime getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDateTime dateDebut) { this.dateDebut = dateDebut; }
    
    public LocalDateTime getDateFin() { return dateFin; }
    public void setDateFin(LocalDateTime dateFin) { this.dateFin = dateFin; }
    
    public String getCommentaires() { return commentaires; }
    public void setCommentaires(String commentaires) { this.commentaires = commentaires; }
    
    public String getDonneesValidation() { return donneesValidation; }
    public void setDonneesValidation(String donneesValidation) { this.donneesValidation = donneesValidation; }
    
    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
    
    public UUID getEtapeSuivanteId() { return etapeSuivanteId; }
    public void setEtapeSuivanteId(UUID etapeSuivanteId) { this.etapeSuivanteId = etapeSuivanteId; }
    
    public String getRaisonRejet() { return raisonRejet; }
    public void setRaisonRejet(String raisonRejet) { this.raisonRejet = raisonRejet; }
    
    public Boolean getDelaiDepasse() { return delaiDepasse; }
    public void setDelaiDepasse(Boolean delaiDepasse) { this.delaiDepasse = delaiDepasse; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public void setWorkflowInstanceId(UUID workflowInstanceId) {
        if (this.workflowInstance == null) {
            this.workflowInstance = new WorkflowInstance();
        }
        this.workflowInstance.setId(workflowInstanceId);
    }
    
    public void setProcessStepId(UUID processStepId) {
        if (this.processStep == null) {
            this.processStep = new ProcessStep();
        }
        this.processStep.setId(processStepId);
    }
}
