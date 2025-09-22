package ga.rdgtt.autoecole.model;

public enum StatutPaiement {
    EN_ATTENTE("En attente"),
    VALIDE("Validé"),
    REFUSE("Refusé"),
    REMBOURSE("Remboursé"),
    ANNULE("Annulé");

    private final String libelle;

    StatutPaiement(String libelle) {
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
