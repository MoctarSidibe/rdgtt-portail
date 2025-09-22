package ga.rdgtt.usager.controller;

import ga.rdgtt.usager.dto.LoginRequest;
import ga.rdgtt.usager.dto.LoginResponse;
import ga.rdgtt.usager.dto.UserDTO;
import ga.rdgtt.usager.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usager/auth")
@Tag(name = "Authentication", description = "API d'authentification des utilisateurs")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    @Operation(summary = "Connexion utilisateur", description = "Authentifie un utilisateur et retourne un token JWT")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/me")
    @Operation(summary = "Utilisateur actuel", description = "Retourne les informations de l'utilisateur connecté")
    public ResponseEntity<UserDTO> getCurrentUser() {
        UserDTO user = authService.getCurrentUser();
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/logout")
    @Operation(summary = "Déconnexion", description = "Déconnecte l'utilisateur actuel")
    public ResponseEntity<String> logout() {
        // In a stateless JWT system, logout is handled client-side by removing the token
        return ResponseEntity.ok("Déconnexion réussie");
    }
}
