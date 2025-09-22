package ga.rdgtt.autoecole.model;

public enum TypePaiement {
    INSCRIPTION("Inscription"),
    FORMATION("Formation"),
    EXAMEN("Examen"),
    PERMIS("Permis"),
    RENOUVELLEMENT("Renouvellement"),
    DUPLICATA("Duplicata"),
    AUTRE("Autre");

    private final String libelle;

    TypePaiement(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}
