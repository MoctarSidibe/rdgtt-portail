package ga.rdgtt.admin.service;

import ga.rdgtt.admin.model.AdminUser;
import ga.rdgtt.admin.model.UserRole;
import ga.rdgtt.admin.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminUserService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<AdminUser> getAllUsers() {
        return adminUserRepository.findAll();
    }

    public Optional<AdminUser> getUserById(UUID id) {
        return adminUserRepository.findById(id);
    }

    public Optional<AdminUser> getUserByEmail(String email) {
        return adminUserRepository.findByEmail(email);
    }

    public AdminUser createUser(AdminUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return adminUserRepository.save(user);
    }

    public AdminUser updateUser(AdminUser user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return adminUserRepository.save(user);
    }

    public void deleteUser(UUID id) {
        adminUserRepository.deleteById(id);
    }

    public List<AdminUser> getUsersByRole(UserRole role) {
        return adminUserRepository.findByRole(role);
    }

    public List<AdminUser> getUsersByDepartment(UUID departmentId) {
        return adminUserRepository.findByDepartementId(departmentId);
    }

    public List<AdminUser> getUsersByBureau(UUID bureauId) {
        return adminUserRepository.findByBureauId(bureauId);
    }

    public boolean existsByEmail(String email) {
        return adminUserRepository.existsByEmail(email);
    }
}
