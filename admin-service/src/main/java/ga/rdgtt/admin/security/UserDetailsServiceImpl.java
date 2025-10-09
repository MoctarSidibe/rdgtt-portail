package ga.rdgtt.admin.security;

import ga.rdgtt.admin.model.AdminUser;
import ga.rdgtt.admin.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser adminUser = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));

        return User.builder()
                .username(adminUser.getUsername())
                .password(adminUser.getPassword())
                .authorities(getAuthorities(adminUser))
                .accountExpired(false)
                .accountLocked(!adminUser.getActif())
                .credentialsExpired(false)
                .disabled(!adminUser.getActif())
                .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(AdminUser adminUser) {
        String role = "ROLE_" + adminUser.getRole().name();
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}

