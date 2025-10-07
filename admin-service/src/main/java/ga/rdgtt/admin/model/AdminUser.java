package ga.rdgtt.admin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "admin_users")
public class AdminUser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotBlank(message = "Le nom est obligatoire")
    @Column(name = "nom", nullable = false)
    private String nom;
    
    @NotBlank(message = "Le prénom est obligatoire")
    @Column(name = "prenom", nullable = false)
    private String prenom;
    
    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    
    @Column(name = "telephone")
    private String telephone;
    
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Column(name = "password", nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role = UserRole.AGENT;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departement_id")
    private Department departement;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bureau_id")
    private Bureau bureau;
    
    @Column(name = "actif", nullable = false)
    private Boolean actif = true;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public AdminUser() {}
    
    public AdminUser(String nom, String prenom, String email, String password, UserRole role) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.role = role;
    }
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    
    public Department getDepartement() { return departement; }
    public void setDepartement(Department departement) { this.departement = departement; }
    
    public Bureau getBureau() { return bureau; }
    public void setBureau(Bureau bureau) { this.bureau = bureau; }
    
    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Business methods
    public String getFullName() {
        return prenom + " " + nom;
    }
    
    public boolean hasRole(UserRole requiredRole) {
        return this.role.getLevel() >= requiredRole.getLevel();
    }
    
    public boolean canManageUsers() {
        return hasRole(UserRole.CHEF_SERVICE);
    }
    
    public boolean canManageWorkflows() {
        return hasRole(UserRole.CHEF_SERVICE);
    }
    
    public boolean canApproveDocuments() {
        return hasRole(UserRole.DGTT);
    }
}


