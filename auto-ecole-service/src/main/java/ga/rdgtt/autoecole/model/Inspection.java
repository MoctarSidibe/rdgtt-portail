package ga.rdgtt.autoecole.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inspections")
public class Inspection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auto_ecole_id", nullable = false)
    private AutoEcole autoEcole;

    @Column(name = "date_inspection", nullable = false)
    private LocalDateTime dateInspection;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutInspection statut;

    @Column(name = "rapport_inspection", columnDefinition = "TEXT")
    private String rapportInspection;

    @Column(name = "inspecteur_nom")
    private String inspecteurNom;

    @Column(name = "inspecteur_prenom")
    private String inspecteurPrenom;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Inspection() {}

    public Inspection(AutoEcole autoEcole, LocalDateTime dateInspection, StatutInspection statut) {
        this.autoEcole = autoEcole;
        this.dateInspection = dateInspection;
        this.statut = statut;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AutoEcole getAutoEcole() {
        return autoEcole;
    }

    public void setAutoEcole(AutoEcole autoEcole) {
        this.autoEcole = autoEcole;
    }

    public LocalDateTime getDateInspection() {
        return dateInspection;
    }

    public void setDateInspection(LocalDateTime dateInspection) {
        this.dateInspection = dateInspection;
    }

    public StatutInspection getStatut() {
        return statut;
    }

    public void setStatut(StatutInspection statut) {
        this.statut = statut;
    }

    public String getRapportInspection() {
        return rapportInspection;
    }

    public void setRapportInspection(String rapportInspection) {
        this.rapportInspection = rapportInspection;
    }

    public String getInspecteurNom() {
        return inspecteurNom;
    }

    public void setInspecteurNom(String inspecteurNom) {
        this.inspecteurNom = inspecteurNom;
    }

    public String getInspecteurPrenom() {
        return inspecteurPrenom;
    }

    public void setInspecteurPrenom(String inspecteurPrenom) {
        this.inspecteurPrenom = inspecteurPrenom;
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

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
