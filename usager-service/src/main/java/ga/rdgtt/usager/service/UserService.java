package ga.rdgtt.usager.service;

import ga.rdgtt.usager.dto.UserDTO;
import ga.rdgtt.usager.model.User;
import ga.rdgtt.usager.repository.UserRepository;
import ga.rdgtt.usager.repository.RoleRepository;
import ga.rdgtt.usager.repository.DepartmentRepository;
import ga.rdgtt.usager.repository.BureauRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private BureauRepository bureauRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        user.setNomFamille(userDTO.getNomFamille());
        user.setNomJeuneFille(userDTO.getNomJeuneFille());
        user.setPrenom(userDTO.getPrenom());
        user.setEmail(userDTO.getEmail());
        user.setTelephone(userDTO.getTelephone());
        user.setPasswordHash(passwordEncoder.encode("password123")); // Default password
        
        // Set role
        if (userDTO.getRoleId() != null) {
            roleRepository.findById(userDTO.getRoleId()).ifPresent(user::setRole);
        }
        
        // Set department
        if (userDTO.getDepartmentId() != null) {
            departmentRepository.findById(userDTO.getDepartmentId()).ifPresent(user::setDepartment);
        }
        
        // Set bureau
        if (userDTO.getBureauId() != null) {
            bureauRepository.findById(userDTO.getBureauId()).ifPresent(user::setBureau);
        }
        
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }
    
    public UserDTO updateUser(UUID id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        user.setNomFamille(userDTO.getNomFamille());
        user.setNomJeuneFille(userDTO.getNomJeuneFille());
        user.setPrenom(userDTO.getPrenom());
        user.setEmail(userDTO.getEmail());
        user.setTelephone(userDTO.getTelephone());
        user.setActif(userDTO.getActif());
        
        // Update role
        if (userDTO.getRoleId() != null) {
            roleRepository.findById(userDTO.getRoleId()).ifPresent(user::setRole);
        }
        
        // Update department
        if (userDTO.getDepartmentId() != null) {
            departmentRepository.findById(userDTO.getDepartmentId()).ifPresent(user::setDepartment);
        }
        
        // Update bureau
        if (userDTO.getBureauId() != null) {
            bureauRepository.findById(userDTO.getBureauId()).ifPresent(user::setBureau);
        }
        
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }
    
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setActif(false);
        userRepository.save(user);
    }
    
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return convertToDTO(user);
    }
    
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return convertToDTO(user);
    }
    
    public List<UserDTO> getAllUsers() {
        return userRepository.findByActifTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<UserDTO> getUsersByRole(String roleCode) {
        return userRepository.findActiveUsersByRole(roleCode).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<UserDTO> getUsersByDepartment(UUID departmentId) {
        return userRepository.findActiveUsersByDepartment(departmentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<UserDTO> getUsersByBureau(UUID bureauId) {
        return userRepository.findActiveUsersByBureau(bureauId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public void updateLastLogin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public long countActiveUsers() {
        return userRepository.countActiveUsers();
    }
    
    public long countActiveUsersByRole(String roleCode) {
        return userRepository.countActiveUsersByRole(roleCode);
    }
    
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setNomFamille(user.getNomFamille());
        dto.setNomJeuneFille(user.getNomJeuneFille());
        dto.setPrenom(user.getPrenom());
        dto.setEmail(user.getEmail());
        dto.setTelephone(user.getTelephone());
        dto.setActif(user.getActif());
        dto.setLastLogin(user.getLastLogin());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        
        if (user.getRole() != null) {
            dto.setRoleId(user.getRole().getId());
            dto.setRoleCode(user.getRole().getCode());
            dto.setRoleNom(user.getRole().getNom());
        }
        
        if (user.getDepartment() != null) {
            dto.setDepartmentId(user.getDepartment().getId());
            dto.setDepartmentCode(user.getDepartment().getCode());
            dto.setDepartmentNom(user.getDepartment().getNom());
        }
        
        if (user.getBureau() != null) {
            dto.setBureauId(user.getBureau().getId());
            dto.setBureauCode(user.getBureau().getCode());
            dto.setBureauNom(user.getBureau().getNom());
        }
        
        return dto;
    }
}
