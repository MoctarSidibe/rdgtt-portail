package ga.rdgtt.autoecole.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "auto_ecole_documents")
public class AutoEcoleDocument {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotNull(message = "L'auto-école est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auto_ecole_id", nullable = false)
    private AutoEcole autoEcole;
    
    @NotBlank(message = "Le type de document est obligatoire")
    @Column(name = "type_document", nullable = false)
    private String typeDocument;
    
    @NotBlank(message = "Le nom du fichier est obligatoire")
    @Column(name = "nom_fichier", nullable = false)
    private String nomFichier;
    
    @NotBlank(message = "Le chemin du fichier est obligatoire")
    @Column(name = "chemin_fichier", nullable = false)
    private String cheminFichier;
    
    @Column(name = "taille_fichier")
    private Long tailleFichier;
    
    @Column(name = "mime_type")
    private String mimeType;
    
    @CreationTimestamp
    @Column(name = "uploaded_at", updatable = false)
    private LocalDateTime uploadedAt;
    
    // Constructors
    public AutoEcoleDocument() {}
    
    public AutoEcoleDocument(AutoEcole autoEcole, String typeDocument, String nomFichier, String cheminFichier) {
        this.autoEcole = autoEcole;
        this.typeDocument = typeDocument;
        this.nomFichier = nomFichier;
        this.cheminFichier = cheminFichier;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public AutoEcole getAutoEcole() {
        return autoEcole;
    }
    
    public void setAutoEcole(AutoEcole autoEcole) {
        this.autoEcole = autoEcole;
    }
    
    public String getTypeDocument() {
        return typeDocument;
    }
    
    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }
    
    public String getNomFichier() {
        return nomFichier;
    }
    
    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }
    
    public String getCheminFichier() {
        return cheminFichier;
    }
    
    public void setCheminFichier(String cheminFichier) {
        this.cheminFichier = cheminFichier;
    }
    
    public Long getTailleFichier() {
        return tailleFichier;
    }
    
    public void setTailleFichier(Long tailleFichier) {
        this.tailleFichier = tailleFichier;
    }
    
    public String getMimeType() {
        return mimeType;
    }
    
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    
    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
    
    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
