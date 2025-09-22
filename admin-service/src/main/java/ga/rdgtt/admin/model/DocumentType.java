package ga.rdgtt.admin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "document_types")
public class DocumentType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotBlank(message = "Le nom du type de document est obligatoire")
    @Column(name = "nom", nullable = false)
    private String nom;
    
    @Column(name = "code", unique = true, nullable = false)
    private String code;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "service_code", nullable = false)
    private String serviceCode; // auto-ecole, permis, carte-grise, etc.
    
    @Column(name = "categorie")
    private String categorie; // principal, connexe, administratif
    
    @Column(name = "actif", nullable = false)
    private Boolean actif = true;
    
    @Column(name = "delai_traitement_jours")
    private Integer delaiTraitementJours;
    
    @Column(name = "frais_obligatoire")
    private Boolean fraisObligatoire = false;
    
    @Column(name = "montant_frais")
    private Double montantFrais;
    
    @Column(name = "documents_requis", columnDefinition = "TEXT")
    private String documentsRequis; // JSON array of required documents
    
    @Column(name = "conditions_eligibilite", columnDefinition = "TEXT")
    private String conditionsEligibilite; // JSON conditions
    
    @Column(name = "workflow_config", columnDefinition = "TEXT")
    private String workflowConfig; // JSON workflow configuration
    
    @OneToMany(mappedBy = "documentType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProcessStep> processSteps;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public DocumentType() {}
    
    public DocumentType(String nom, String code, String serviceCode) {
        this.nom = nom;
        this.code = code;
        this.serviceCode = serviceCode;
    }
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getServiceCode() { return serviceCode; }
    public void setServiceCode(String serviceCode) { this.serviceCode = serviceCode; }
    
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    
    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }
    
    public Integer getDelaiTraitementJours() { return delaiTraitementJours; }
    public void setDelaiTraitementJours(Integer delaiTraitementJours) { this.delaiTraitementJours = delaiTraitementJours; }
    
    public Boolean getFraisObligatoire() { return fraisObligatoire; }
    public void setFraisObligatoire(Boolean fraisObligatoire) { this.fraisObligatoire = fraisObligatoire; }
    
    public Double getMontantFrais() { return montantFrais; }
    public void setMontantFrais(Double montantFrais) { this.montantFrais = montantFrais; }
    
    public String getDocumentsRequis() { return documentsRequis; }
    public void setDocumentsRequis(String documentsRequis) { this.documentsRequis = documentsRequis; }
    
    public String getConditionsEligibilite() { return conditionsEligibilite; }
    public void setConditionsEligibilite(String conditionsEligibilite) { this.conditionsEligibilite = conditionsEligibilite; }
    
    public String getWorkflowConfig() { return workflowConfig; }
    public void setWorkflowConfig(String workflowConfig) { this.workflowConfig = workflowConfig; }
    
    public List<ProcessStep> getProcessSteps() { return processSteps; }
    public void setProcessSteps(List<ProcessStep> processSteps) { this.processSteps = processSteps; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
