package ga.rdgtt.usager.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "document_types")
public class DocumentType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(name = "code", unique = true, nullable = false)
    private String code;
    
    @Column(name = "nom", nullable = false)
    private String nom;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "prix", precision = 10, scale = 2)
    private Double prix;
    
    @Column(name = "delai_traitement_jours")
    private Integer delaiTraitementJours;
    
    @Column(name = "actif")
    private Boolean actif = true;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public DocumentType() {}
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }
    
    public Integer getDelaiTraitementJours() { return delaiTraitementJours; }
    public void setDelaiTraitementJours(Integer delaiTraitementJours) { this.delaiTraitementJours = delaiTraitementJours; }
    
    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
