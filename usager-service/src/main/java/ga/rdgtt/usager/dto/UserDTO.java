package ga.rdgtt.usager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserDTO {
    
    private Long id;
    
    @NotBlank(message = "Le nom de famille est obligatoire")
    private String nomFamille;
    
    private String nomJeuneFille;
    
    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;
    
    @Email(message = "L'email doit être valide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;
    
    private String telephone;
    
    @NotNull(message = "Le rôle est obligatoire")
    private UUID roleId;
    
    private String roleCode;
    
    private String roleNom;
    
    private UUID departmentId;
    
    private String departmentCode;
    
    private String departmentNom;
    
    private UUID bureauId;
    
    private String bureauCode;
    
    private String bureauNom;
    
    private Boolean actif;
    
    private LocalDateTime lastLogin;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Constructors
    public UserDTO() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNomFamille() {
        return nomFamille;
    }
    
    public void setNomFamille(String nomFamille) {
        this.nomFamille = nomFamille;
    }
    
    public String getNomJeuneFille() {
        return nomJeuneFille;
    }
    
    public void setNomJeuneFille(String nomJeuneFille) {
        this.nomJeuneFille = nomJeuneFille;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public UUID getRoleId() {
        return roleId;
    }
    
    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }
    
    public String getRoleCode() {
        return roleCode;
    }
    
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
    
    public String getRoleNom() {
        return roleNom;
    }
    
    public void setRoleNom(String roleNom) {
        this.roleNom = roleNom;
    }
    
    public UUID getDepartmentId() {
        return departmentId;
    }
    
    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }
    
    public String getDepartmentCode() {
        return departmentCode;
    }
    
    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }
    
    public String getDepartmentNom() {
        return departmentNom;
    }
    
    public void setDepartmentNom(String departmentNom) {
        this.departmentNom = departmentNom;
    }
    
    public UUID getBureauId() {
        return bureauId;
    }
    
    public void setBureauId(UUID bureauId) {
        this.bureauId = bureauId;
    }
    
    public String getBureauCode() {
        return bureauCode;
    }
    
    public void setBureauCode(String bureauCode) {
        this.bureauCode = bureauCode;
    }
    
    public String getBureauNom() {
        return bureauNom;
    }
    
    public void setBureauNom(String bureauNom) {
        this.bureauNom = bureauNom;
    }
    
    public Boolean getActif() {
        return actif;
    }
    
    public void setActif(Boolean actif) {
        this.actif = actif;
    }
    
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
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
    
    // Helper methods
    public String getFullName() {
        return prenom + " " + nomFamille;
    }
}
