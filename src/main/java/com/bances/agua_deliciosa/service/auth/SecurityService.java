package com.bances.agua_deliciosa.service.auth;

import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.RoleRepository;
import com.bances.agua_deliciosa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;

    public Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public User getUser() {
        Authentication auth = getCurrentAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        return userRepository.findByEmail(auth.getName()).orElse(null);
    }

    public boolean isAuthenticated() {
        Authentication authentication = getCurrentAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    public Authentication authenticate(String email, String password) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            return auth;
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<String> getRoles(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        return roleRepository.findById(user.getRoleId())
            .map(Role::getName)
            .map(List::of)
            .orElse(List.of());
    }

    @Transactional(readOnly = true)
    public boolean hasRole(Long userId, String roleName) {
        return getRoles(userId)
            .stream()
            .anyMatch(role -> role.equals(roleName));
    }

    public boolean isEmailVerified(String email) {
        return userRepository.findByEmail(email)
                .map(user -> user.getEmailVerifiedAt() != null)
                .orElse(false);
    }
}
