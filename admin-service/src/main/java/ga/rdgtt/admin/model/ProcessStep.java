package ga.rdgtt.admin.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "process_steps")
public class ProcessStep {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_type_id", nullable = false)
    private DocumentType documentType;
    
    @Column(name = "nom", nullable = false)
    private String nom;
    
    @Column(name = "code", nullable = false)
    private String code;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "ordre", nullable = false)
    private Integer ordre;
    
    @Column(name = "departement_id")
    private UUID departementId;
    
    @Column(name = "bureau_id")
    private UUID bureauId;
    
    @Column(name = "role_requis")
    private String roleRequis;
    
    @Column(name = "type_validation")
    private String typeValidation;
    
    @Column(name = "conditions_validation", columnDefinition = "TEXT")
    private String conditionsValidation;
    
    @Column(name = "delai_jours")
    private Integer delaiJours = 5;
    
    @Column(name = "etape_suivante_id")
    private UUID etapeSuivanteId;
    
    @Column(name = "etape_rejet_id")
    private UUID etapeRejetId;
    
    @Column(name = "actif")
    private Boolean actif = true;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public ProcessStep() {}
    
    public ProcessStep(DocumentType documentType, String nom, String code, Integer ordre) {
        this.documentType = documentType;
        this.nom = nom;
        this.code = code;
        this.ordre = ordre;
    }
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public DocumentType getDocumentType() { return documentType; }
    public void setDocumentType(DocumentType documentType) { this.documentType = documentType; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Integer getOrdre() { return ordre; }
    public void setOrdre(Integer ordre) { this.ordre = ordre; }
    
    public UUID getDepartementId() { return departementId; }
    public void setDepartementId(UUID departementId) { this.departementId = departementId; }
    
    public UUID getBureauId() { return bureauId; }
    public void setBureauId(UUID bureauId) { this.bureauId = bureauId; }
    
    public String getRoleRequis() { return roleRequis; }
    public void setRoleRequis(String roleRequis) { this.roleRequis = roleRequis; }
    
    public String getTypeValidation() { return typeValidation; }
    public void setTypeValidation(String typeValidation) { this.typeValidation = typeValidation; }
    
    public String getConditionsValidation() { return conditionsValidation; }
    public void setConditionsValidation(String conditionsValidation) { this.conditionsValidation = conditionsValidation; }
    
    public Integer getDelaiJours() { return delaiJours; }
    public void setDelaiJours(Integer delaiJours) { this.delaiJours = delaiJours; }
    
    public UUID getEtapeSuivanteId() { return etapeSuivanteId; }
    public void setEtapeSuivanteId(UUID etapeSuivanteId) { this.etapeSuivanteId = etapeSuivanteId; }
    
    public UUID getEtapeRejetId() { return etapeRejetId; }
    public void setEtapeRejetId(UUID etapeRejetId) { this.etapeRejetId = etapeRejetId; }
    
    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Add method to get documentTypeId for repository queries
    public UUID getDocumentTypeId() {
        return documentType != null ? documentType.getId() : null;
    }
}