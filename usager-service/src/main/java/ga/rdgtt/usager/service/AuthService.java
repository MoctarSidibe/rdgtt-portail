package ga.rdgtt.usager.service;

import ga.rdgtt.usager.dto.LoginRequest;
import ga.rdgtt.usager.dto.LoginResponse;
import ga.rdgtt.usager.dto.UserDTO;
import ga.rdgtt.usager.security.UserDetailsImpl;
import ga.rdgtt.usager.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserService userService;
    
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(userDetails);
        
        // Update last login
        userService.updateLastLogin(userDetails.getUser().getId());
        
        UserDTO userDTO = userService.getUserById(userDetails.getUser().getId());
        
        return new LoginResponse(jwt, userDTO, jwtUtil.getExpirationDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
    }
    
    public UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return userService.getUserById(userDetails.getUser().getId());
        }
        return null;
    }
}
