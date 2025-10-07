package ga.rdgtt.admin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    
    @NotBlank(message = "Le nom de l'étape est obligatoire")
    @Column(name = "nom", nullable = false)
    private String nom;
    
    @Column(name = "code", nullable = false)
    private String code;
    
    @Column(name = "description")
    private String description;
    
    @NotNull(message = "L'ordre de l'étape est obligatoire")
    @Column(name = "ordre", nullable = false)
    private Integer ordre;
    
    @Column(name = "departement_id")
    private UUID departementId;
    
    @Column(name = "bureau_id")
    private UUID bureauId;
    
    @Column(name = "role_requis")
    private String roleRequis; // ADMIN, DC, SEV, SAF, etc.
    
    @Column(name = "type_validation")
    private String typeValidation; // automatique, manuelle, hybride
    
    @Column(name = "conditions_validation", columnDefinition = "TEXT")
    private String conditionsValidation; // JSON conditions
    
    @Column(name = "delai_max_jours")
    private Integer delaiMaxJours;
    
    @Column(name = "obligatoire", nullable = false)
    private Boolean obligatoire = true;
    
    @Column(name = "peut_rejeter", nullable = false)
    private Boolean peutRejeter = true;
    
    @Column(name = "peut_rediriger", nullable = false)
    private Boolean peutRediriger = false;
    
    @Column(name = "etape_suivante_id")
    private UUID etapeSuivanteId;
    
    @Column(name = "etape_rejet_id")
    private UUID etapeRejetId;
    
    @Column(name = "actif", nullable = false)
    private Boolean actif = true;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public ProcessStep() {}
    
    public ProcessStep(String nom, String code, Integer ordre, String typeValidation) {
        this.nom = nom;
        this.code = code;
        this.ordre = ordre;
        this.typeValidation = typeValidation;
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
    
    public Integer getDelaiMaxJours() { return delaiMaxJours; }
    public void setDelaiMaxJours(Integer delaiMaxJours) { this.delaiMaxJours = delaiMaxJours; }
    
    public Boolean getObligatoire() { return obligatoire; }
    public void setObligatoire(Boolean obligatoire) { this.obligatoire = obligatoire; }
    
    public Boolean getPeutRejeter() { return peutRejeter; }
    public void setPeutRejeter(Boolean peutRejeter) { this.peutRejeter = peutRejeter; }
    
    public Boolean getPeutRediriger() { return peutRediriger; }
    public void setPeutRediriger(Boolean peutRediriger) { this.peutRediriger = peutRediriger; }
    
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
    
    public UUID getDocumentTypeId() {
        return documentType != null ? documentType.getId() : null;
    }
}
