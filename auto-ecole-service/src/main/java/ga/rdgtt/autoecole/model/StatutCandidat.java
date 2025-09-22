package ga.rdgtt.autoecole.model;

public enum StatutCandidat {
    EN_ATTENTE("En attente"),
    EN_FORMATION("En formation"),
    CODE_EN_COURS("Code en cours"),
    CODE_VALIDE("Code validé"),
    CRENEAU_EN_COURS("Créneau en cours"),
    CRENEAU_VALIDE("Créneau validé"),
    PRATIQUE_EN_COURS("Pratique en cours"),
    PRATIQUE_VALIDE("Pratique validé"),
    ADMIS("Admis"),
    SUSPENDU("Suspendu"),
    REJETE("Rejeté");
    
    private final String description;
    
    StatutCandidat(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
