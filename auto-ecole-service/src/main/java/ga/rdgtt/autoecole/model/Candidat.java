package ga.rdgtt.autoecole.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "candidats")
public class Candidat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotBlank(message = "Le nom de famille est obligatoire")
    @Column(name = "nom_famille", nullable = false)
    private String nomFamille;
    
    @Column(name = "nom_jeune_fille")
    private String nomJeuneFille;
    
    @NotBlank(message = "Le prénom est obligatoire")
    @Column(name = "prenom", nullable = false)
    private String prenom;
    
    @NotNull(message = "La date de naissance est obligatoire")
    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;
    
    @NotBlank(message = "Le lieu de naissance est obligatoire")
    @Column(name = "lieu_naissance", nullable = false)
    private String lieuNaissance;
    
    @NotBlank(message = "La nationalité est obligatoire")
    @Column(name = "nationalite", nullable = false)
    private String nationalite;
    
    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Column(name = "telephone", nullable = false)
    private String telephone;
    
    @Email(message = "L'email doit être valide")
    @Column(name = "email")
    private String email;
    
    @Column(name = "photo")
    private String photo;
    
    @NotNull(message = "L'auto-école est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auto_ecole_id", nullable = false)
    private AutoEcole autoEcole;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutCandidat statut = StatutCandidat.EN_ATTENTE;
    
    @Column(name = "numero_dossier")
    private String numeroDossier;
    
    @Column(name = "numero_license")
    private String numeroLicense;
    
    @Column(name = "commentaire", columnDefinition = "TEXT")
    private String commentaire;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CandidatDocument> documents;
    
    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Examen> examens;
    
    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Paiement> paiements;
    
    // Constructors
    public Candidat() {}
    
    public Candidat(String nomFamille, String prenom, LocalDate dateNaissance, String lieuNaissance, 
                   String nationalite, String telephone, AutoEcole autoEcole) {
        this.nomFamille = nomFamille;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.lieuNaissance = lieuNaissance;
        this.nationalite = nationalite;
        this.telephone = telephone;
        this.autoEcole = autoEcole;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getNomFamille() {
        return nomFamille;
    }
    
    public void setNomFamille(String nomFamille) {
        this.nomFamille = nomFamille;
    }
    
    public String getNomJeuneFille() {
        return nomJeuneFille;
    }
    
    public void setNomJeuneFille(String nomJeuneFille) {
        this.nomJeuneFille = nomJeuneFille;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public LocalDate getDateNaissance() {
        return dateNaissance;
    }
    
    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
    
    public String getLieuNaissance() {
        return lieuNaissance;
    }
    
    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }
    
    public String getNationalite() {
        return nationalite;
    }
    
    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoto() {
        return photo;
    }
    
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    
    public AutoEcole getAutoEcole() {
        return autoEcole;
    }
    
    public void setAutoEcole(AutoEcole autoEcole) {
        this.autoEcole = autoEcole;
    }
    
    public StatutCandidat getStatut() {
        return statut;
    }
    
    public void setStatut(StatutCandidat statut) {
        this.statut = statut;
    }
    
    public String getNumeroDossier() {
        return numeroDossier;
    }
    
    public void setNumeroDossier(String numeroDossier) {
        this.numeroDossier = numeroDossier;
    }
    
    public String getNumeroLicense() {
        return numeroLicense;
    }
    
    public void setNumeroLicense(String numeroLicense) {
        this.numeroLicense = numeroLicense;
    }
    
    public String getCommentaire() {
        return commentaire;
    }
    
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public List<CandidatDocument> getDocuments() {
        return documents;
    }
    
    public void setDocuments(List<CandidatDocument> documents) {
        this.documents = documents;
    }
    
    public List<Examen> getExamens() {
        return examens;
    }
    
    public void setExamens(List<Examen> examens) {
        this.examens = examens;
    }
    
    public List<Paiement> getPaiements() {
        return paiements;
    }
    
    public void setPaiements(List<Paiement> paiements) {
        this.paiements = paiements;
    }
    
    // Helper methods
    public String getFullName() {
        return prenom + " " + nomFamille;
    }
    
    public int getAge() {
        return LocalDate.now().getYear() - dateNaissance.getYear();
    }
}
