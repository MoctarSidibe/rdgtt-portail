package ga.rdgtt.admin.model;

public enum UserRole {
    
    // Role hierarchy from highest to lowest authority
    DGTT("Directeur Général", 5, "Direction Générale des Transports Terrestres"),
    DIRECTEUR("Directeur", 4, "Directeur de département"),
    CHEF_SERVICE("Chef de Service", 3, "Chef de service"),
    CHEF_BUREAU("Chef de Bureau", 2, "Chef de bureau"),
    AGENT("Agent", 1, "Agent administratif");
    
    private final String displayName;
    private final int level;
    private final String description;
    
    UserRole(String displayName, int level, String description) {
        this.displayName = displayName;
        this.level = level;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getLevel() {
        return level;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Check if this role can manage another role
     */
    public boolean canManage(UserRole otherRole) {
        return this.level > otherRole.level;
    }
    
    /**
     * Check if this role can approve documents
     */
    public boolean canApprove() {
        return this.level >= CHEF_SERVICE.level;
    }
    
    /**
     * Check if this role can create workflows
     */
    public boolean canCreateWorkflows() {
        return this.level >= CHEF_SERVICE.level;
    }
    
    /**
     * Check if this role can manage departments
     */
    public boolean canManageDepartments() {
        return this.level >= DIRECTEUR.level;
    }
    
    /**
     * Check if this role can manage bureaus
     */
    public boolean canManageBureaus() {
        return this.level >= CHEF_SERVICE.level;
    }
    
    /**
     * Get all roles that this role can manage
     */
    public UserRole[] getManageableRoles() {
        return java.util.Arrays.stream(values())
                .filter(role -> this.canManage(role))
                .toArray(UserRole[]::new);
    }
}


