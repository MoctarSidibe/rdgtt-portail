package ga.rdgtt.autoecole.model;

public enum StatutExamen {
    PROGRAMME("Programmé"),
    EN_COURS("En cours"),
    TERMINE("Terminé"),
    REUSSI("Réussi"),
    ECHOUE("Échoué"),
    REPORTE("Reporté"),
    ANNULE("Annulé");

    private final String libelle;

    StatutExamen(String libelle) {
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
