package ga.rdgtt.usager.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(name = "user_id")
    private UUID userId;
    
    @Column(name = "application_id")
    private UUID applicationId;
    
    @Column(name = "demande_id", nullable = false)
    private String demandeId;
    
    @Column(name = "document_type_id")
    private UUID documentTypeId;
    
    @Column(name = "montant", nullable = false)
    private Double montant;
    
    @Column(name = "devise")
    private String devise = "XAF";
    
    @Column(name = "payment_method_id")
    private UUID paymentMethodId;
    
    @Column(name = "statut")
    private String statut = "EN_ATTENTE";
    
    @Column(name = "reference_paiement")
    private String referencePaiement;
    
    @Column(name = "reference_externe")
    private String referenceExterne;
    
    @Column(name = "date_paiement")
    private LocalDateTime datePaiement;
    
    @Column(name = "date_expiration")
    private LocalDateTime dateExpiration;
    
    @Column(name = "donnees_paiement", columnDefinition = "TEXT")
    private String donneesPaiement;
    
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
    @JoinColumn(name = "payment_method_id", insertable = false, updatable = false)
    private PaymentMethod paymentMethod;
    
    // Constructors
    public Payment() {}
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    
    public UUID getApplicationId() { return applicationId; }
    public void setApplicationId(UUID applicationId) { this.applicationId = applicationId; }
    
    public String getDemandeId() { return demandeId; }
    public void setDemandeId(String demandeId) { this.demandeId = demandeId; }
    
    public UUID getDocumentTypeId() { return documentTypeId; }
    public void setDocumentTypeId(UUID documentTypeId) { this.documentTypeId = documentTypeId; }
    
    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }
    
    public String getDevise() { return devise; }
    public void setDevise(String devise) { this.devise = devise; }
    
    public UUID getPaymentMethodId() { return paymentMethodId; }
    public void setPaymentMethodId(UUID paymentMethodId) { this.paymentMethodId = paymentMethodId; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    public String getReferencePaiement() { return referencePaiement; }
    public void setReferencePaiement(String referencePaiement) { this.referencePaiement = referencePaiement; }
    
    public String getReferenceExterne() { return referenceExterne; }
    public void setReferenceExterne(String referenceExterne) { this.referenceExterne = referenceExterne; }
    
    public LocalDateTime getDatePaiement() { return datePaiement; }
    public void setDatePaiement(LocalDateTime datePaiement) { this.datePaiement = datePaiement; }
    
    public LocalDateTime getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(LocalDateTime dateExpiration) { this.dateExpiration = dateExpiration; }
    
    public String getDonneesPaiement() { return donneesPaiement; }
    public void setDonneesPaiement(String donneesPaiement) { this.donneesPaiement = donneesPaiement; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public DocumentType getDocumentType() { return documentType; }
    public void setDocumentType(DocumentType documentType) { this.documentType = documentType; }
    
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
}
