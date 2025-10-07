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
@Table(name = "bureaus")
public class Bureau {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotBlank(message = "Le nom du bureau est obligatoire")
    @Column(name = "nom", nullable = false)
    private String nom;
    
    @NotBlank(message = "Le code du bureau est obligatoire")
    @Column(name = "code", unique = true, nullable = false)
    private String code;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departement_id", nullable = false)
    private Department departement;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chef_bureau_id")
    private AdminUser chefBureau;
    
    @Column(name = "actif", nullable = false)
    private Boolean actif = true;
    
    @OneToMany(mappedBy = "bureau", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AdminUser> users = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Bureau() {}
    
    public Bureau(String nom, String code, String description, Department departement) {
        this.nom = nom;
        this.code = code;
        this.description = description;
        this.departement = departement;
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
    
    public Department getDepartement() { return departement; }
    public void setDepartement(Department departement) { this.departement = departement; }
    
    public AdminUser getChefBureau() { return chefBureau; }
    public void setChefBureau(AdminUser chefBureau) { this.chefBureau = chefBureau; }
    
    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }
    
    public List<AdminUser> getUsers() { return users; }
    public void setUsers(List<AdminUser> users) { this.users = users; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Business methods
    public void addUser(AdminUser user) {
        users.add(user);
        user.setBureau(this);
    }
    
    public void removeUser(AdminUser user) {
        users.remove(user);
        user.setBureau(null);
    }
    
    public String getFullName() {
        return nom + " (" + departement.getNom() + ")";
    }
}


