package ga.rdgtt.usager.controller;

import ga.rdgtt.usager.dto.UserDTO;
import ga.rdgtt.usager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/usager/users")
@Tag(name = "Users", description = "API de gestion des utilisateurs")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    @Operation(summary = "Créer un utilisateur", description = "Crée un nouvel utilisateur")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    
    @GetMapping
    @Operation(summary = "Lister tous les utilisateurs", description = "Retourne la liste de tous les utilisateurs actifs")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un utilisateur", description = "Retourne les informations d'un utilisateur par son ID")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/email/{email}")
    @Operation(summary = "Obtenir un utilisateur par email", description = "Retourne les informations d'un utilisateur par son email")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/role/{roleCode}")
    @Operation(summary = "Utilisateurs par rôle", description = "Retourne la liste des utilisateurs ayant un rôle spécifique")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable String roleCode) {
        List<UserDTO> users = userService.getUsersByRole(roleCode);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Utilisateurs par département", description = "Retourne la liste des utilisateurs d'un département")
    public ResponseEntity<List<UserDTO>> getUsersByDepartment(@PathVariable UUID departmentId) {
        List<UserDTO> users = userService.getUsersByDepartment(departmentId);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/bureau/{bureauId}")
    @Operation(summary = "Utilisateurs par bureau", description = "Retourne la liste des utilisateurs d'un bureau")
    public ResponseEntity<List<UserDTO>> getUsersByBureau(@PathVariable UUID bureauId) {
        List<UserDTO> users = userService.getUsersByBureau(bureauId);
        return ResponseEntity.ok(users);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Modifier un utilisateur", description = "Met à jour les informations d'un utilisateur")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID id, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un utilisateur", description = "Désactive un utilisateur (soft delete)")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/stats/count")
    @Operation(summary = "Nombre d'utilisateurs", description = "Retourne le nombre total d'utilisateurs actifs")
    public ResponseEntity<Long> getUsersCount() {
        long count = userService.countActiveUsers();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/stats/count/role/{roleCode}")
    @Operation(summary = "Nombre d'utilisateurs par rôle", description = "Retourne le nombre d'utilisateurs actifs pour un rôle")
    public ResponseEntity<Long> getUsersCountByRole(@PathVariable String roleCode) {
        long count = userService.countActiveUsersByRole(roleCode);
        return ResponseEntity.ok(count);
    }
}
