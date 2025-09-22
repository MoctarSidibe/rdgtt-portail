package ga.rdgtt.autoecole.model;

public enum StatutInspection {
    PROGRAMMEE("Programmée"),
    EN_COURS("En cours"),
    TERMINEE("Terminée"),
    VALIDEE("Validée"),
    REJETEE("Rejetée"),
    REPORTEE("Reportée");

    private final String libelle;

    StatutInspection(String libelle) {
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
