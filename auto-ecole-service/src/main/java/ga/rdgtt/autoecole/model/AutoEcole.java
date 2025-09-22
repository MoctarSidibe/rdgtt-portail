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
@Table(name = "auto_ecoles")
public class AutoEcole {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotBlank(message = "Le nom de l'auto-école est obligatoire")
    @Column(name = "nom", nullable = false)
    private String nom;
    
    @NotBlank(message = "L'adresse est obligatoire")
    @Column(name = "adresse", nullable = false, columnDefinition = "TEXT")
    private String adresse;
    
    @Column(name = "telephone")
    private String telephone;
    
    @Email(message = "L'email doit être valide")
    @Column(name = "email")
    private String email;
    
    @NotBlank(message = "Le nom du directeur est obligatoire")
    @Column(name = "directeur_nom", nullable = false)
    private String directeurNom;
    
    @Column(name = "directeur_telephone")
    private String directeurTelephone;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutAutoEcole statut = StatutAutoEcole.EN_ATTENTE;
    
    @Column(name = "numero_agrement")
    private String numeroAgrement;
    
    @Column(name = "date_agrement")
    private LocalDate dateAgrement;
    
    @Column(name = "date_expiration")
    private LocalDate dateExpiration;
    
    @NotNull(message = "L'utilisateur administrateur est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "autoEcole", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AutoEcoleDocument> documents;
    
    @OneToMany(mappedBy = "autoEcole", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Candidat> candidats;
    
    @OneToMany(mappedBy = "autoEcole", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inspection> inspections;
    
    // Constructors
    public AutoEcole() {}
    
    public AutoEcole(String nom, String adresse, String directeurNom, User user) {
        this.nom = nom;
        this.adresse = adresse;
        this.directeurNom = directeurNom;
        this.user = user;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getAdresse() {
        return adresse;
    }
    
    public void setAdresse(String adresse) {
        this.adresse = adresse;
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
    
    public String getDirecteurNom() {
        return directeurNom;
    }
    
    public void setDirecteurNom(String directeurNom) {
        this.directeurNom = directeurNom;
    }
    
    public String getDirecteurTelephone() {
        return directeurTelephone;
    }
    
    public void setDirecteurTelephone(String directeurTelephone) {
        this.directeurTelephone = directeurTelephone;
    }
    
    public StatutAutoEcole getStatut() {
        return statut;
    }
    
    public void setStatut(StatutAutoEcole statut) {
        this.statut = statut;
    }
    
    public String getNumeroAgrement() {
        return numeroAgrement;
    }
    
    public void setNumeroAgrement(String numeroAgrement) {
        this.numeroAgrement = numeroAgrement;
    }
    
    public LocalDate getDateAgrement() {
        return dateAgrement;
    }
    
    public void setDateAgrement(LocalDate dateAgrement) {
        this.dateAgrement = dateAgrement;
    }
    
    public LocalDate getDateExpiration() {
        return dateExpiration;
    }
    
    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
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
    
    public List<AutoEcoleDocument> getDocuments() {
        return documents;
    }
    
    public void setDocuments(List<AutoEcoleDocument> documents) {
        this.documents = documents;
    }
    
    public List<Candidat> getCandidats() {
        return candidats;
    }
    
    public void setCandidats(List<Candidat> candidats) {
        this.candidats = candidats;
    }
    
    public List<Inspection> getInspections() {
        return inspections;
    }
    
    public void setInspections(List<Inspection> inspections) {
        this.inspections = inspections;
    }
    
    // Helper methods
    public boolean isActive() {
        return statut == StatutAutoEcole.APPROUVE;
    }
    
    public boolean isEnAttente() {
        return statut == StatutAutoEcole.EN_ATTENTE;
    }
    
    public boolean isRejete() {
        return statut == StatutAutoEcole.REJETE;
    }
}
