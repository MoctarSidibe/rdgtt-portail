package ga.rdgtt.autoecole.model;

public enum StatutAutoEcole {
    EN_ATTENTE("En attente"),
    EN_REVUE("En revue"),
    INSPECTION_PREVUE("Inspection prévue"),
    INSPECTION_EFFECTUEE("Inspection effectuée"),
    APPROUVE("Approuvé"),
    REJETE("Rejeté"),
    SUSPENDU("Suspendu");
    
    private final String description;
    
    StatutAutoEcole(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
