package ga.rdgtt.admin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "departments")
public class Department {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotBlank(message = "Le nom du département est obligatoire")
    @Column(name = "nom", nullable = false)
    private String nom;
    
    @NotBlank(message = "Le code du département est obligatoire")
    @Column(name = "code", unique = true, nullable = false)
    private String code;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chef_departement_id")
    private AdminUser chefDepartement;
    
    @Column(name = "actif", nullable = false)
    private Boolean actif = true;
    
    @OneToMany(mappedBy = "departement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Bureau> bureaus = new ArrayList<>();
    
    @OneToMany(mappedBy = "departement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AdminUser> users = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Department() {}
    
    public Department(String nom, String code, String description) {
        this.nom = nom;
        this.code = code;
        this.description = description;
    }
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public AdminUser getChefDepartement() { return chefDepartement; }
    public void setChefDepartement(AdminUser chefDepartement) { this.chefDepartement = chefDepartement; }
    
    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }
    
    public List<Bureau> getBureaus() { return bureaus; }
    public void setBureaus(List<Bureau> bureaus) { this.bureaus = bureaus; }
    
    public List<AdminUser> getUsers() { return users; }
    public void setUsers(List<AdminUser> users) { this.users = users; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Business methods
    public void addBureau(Bureau bureau) {
        bureaus.add(bureau);
        bureau.setDepartement(this);
    }
    
    public void removeBureau(Bureau bureau) {
        bureaus.remove(bureau);
        bureau.setDepartement(null);
    }
    
    public void addUser(AdminUser user) {
        users.add(user);
        user.setDepartement(this);
    }
    
    public void removeUser(AdminUser user) {
        users.remove(user);
        user.setDepartement(null);
    }
}


