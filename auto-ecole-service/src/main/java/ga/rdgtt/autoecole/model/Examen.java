package ga.rdgtt.autoecole.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "examens")
public class Examen {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidat_id", nullable = false)
    private Candidat candidat;

    @Column(name = "type_examen", nullable = false)
    private String typeExamen;

    @Column(name = "date_examen", nullable = false)
    private LocalDateTime dateExamen;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutExamen statut;

    @Column(name = "note_theorique")
    private Double noteTheorique;

    @Column(name = "note_pratique")
    private Double notePratique;

    @Column(name = "note_finale")
    private Double noteFinale;

    @Column(name = "examinateur_nom")
    private String examinateurNom;

    @Column(name = "examinateur_prenom")
    private String examinateurPrenom;

    @Column(name = "commentaires", columnDefinition = "TEXT")
    private String commentaires;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Examen() {}

    public Examen(Candidat candidat, String typeExamen, LocalDateTime dateExamen, StatutExamen statut) {
        this.candidat = candidat;
        this.typeExamen = typeExamen;
        this.dateExamen = dateExamen;
        this.statut = statut;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Candidat getCandidat() {
        return candidat;
    }

    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
    }

    public String getTypeExamen() {
        return typeExamen;
    }

    public void setTypeExamen(String typeExamen) {
        this.typeExamen = typeExamen;
    }

    public LocalDateTime getDateExamen() {
        return dateExamen;
    }

    public void setDateExamen(LocalDateTime dateExamen) {
        this.dateExamen = dateExamen;
    }

    public StatutExamen getStatut() {
        return statut;
    }

    public void setStatut(StatutExamen statut) {
        this.statut = statut;
    }

    public Double getNoteTheorique() {
        return noteTheorique;
    }

    public void setNoteTheorique(Double noteTheorique) {
        this.noteTheorique = noteTheorique;
    }

    public Double getNotePratique() {
        return notePratique;
    }

    public void setNotePratique(Double notePratique) {
        this.notePratique = notePratique;
    }

    public Double getNoteFinale() {
        return noteFinale;
    }

    public void setNoteFinale(Double noteFinale) {
        this.noteFinale = noteFinale;
    }

    public String getExaminateurNom() {
        return examinateurNom;
    }

    public void setExaminateurNom(String examinateurNom) {
        this.examinateurNom = examinateurNom;
    }

    public String getExaminateurPrenom() {
        return examinateurPrenom;
    }

    public void setExaminateurPrenom(String examinateurPrenom) {
        this.examinateurPrenom = examinateurPrenom;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
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
