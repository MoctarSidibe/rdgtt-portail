package ga.rdgtt.usager.dto;

import java.time.LocalDateTime;

public class LoginResponse {
    
    private String token;
    private String type = "Bearer";
    private UserDTO user;
    private LocalDateTime expiresAt;
    
    // Constructors
    public LoginResponse() {}
    
    public LoginResponse(String token, UserDTO user, LocalDateTime expiresAt) {
        this.token = token;
        this.user = user;
        this.expiresAt = expiresAt;
    }
    
    // Getters and Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public UserDTO getUser() {
        return user;
    }
    
    public void setUser(UserDTO user) {
        this.user = user;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
