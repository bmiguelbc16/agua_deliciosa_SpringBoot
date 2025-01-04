package com.bances.agua_deliciosa.service.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.bances.agua_deliciosa.model.User;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import com.bances.agua_deliciosa.repository.UserRepository;

@Service
@Slf4j
public class SecurityService {
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private UserRepository userRepository;
    
    public Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    
    public User getCurrentUser() {
        try {
            Authentication auth = getCurrentAuthentication();
            if (auth != null && auth.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) auth.getPrincipal();
                return userRepository.findByEmail(userDetails.getUsername())
                    .orElse(null);
            }
            return null;
        } catch (Exception e) {
            log.error("Error getting current user", e);
            return null;
        }
    }
    
    public boolean hasRole(String role) {
        Authentication auth = getCurrentAuthentication();
        return auth != null && auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_" + role.toUpperCase()));
    }
    
    public boolean hasAnyRole(String... roles) {
        Authentication auth = getCurrentAuthentication();
        if (auth == null) return false;
        
        Set<String> userRoles = auth.getAuthorities().stream()
            .map(ga -> ga.getAuthority().replace("ROLE_", ""))
            .collect(Collectors.toSet());
            
        for (String role : roles) {
            if (userRoles.contains(role.toUpperCase())) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isAuthenticated() {
        Authentication auth = getCurrentAuthentication();
        return auth != null && auth.isAuthenticated();
    }
    
    public boolean isAdmin() {
        return hasRole("ADMIN");
    }
    
    public boolean isClient() {
        return hasRole("CLIENTE");
    }
    
    public boolean hasAccess(String userType, Long resourceId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) return false;
        
        if (isAdmin()) return true;
        
        return currentUser.getUserableType().equals(userType) && 
               currentUser.getUserableId().equals(resourceId);
    }
    
    public boolean isEmailVerified(String email) {
        try {
            return userRepository.findByEmail(email)
                .map(user -> user.getEmailVerifiedAt() != null)
                .orElse(false);
        } catch (Exception e) {
            log.error("Error checking email verification status", e);
            return false;
        }
    }
    
    public Set<String> getCurrentUserRoles() {
        Authentication auth = getCurrentAuthentication();
        if (auth == null) return Set.of();
        
        return auth.getAuthorities().stream()
            .map(ga -> ga.getAuthority().replace("ROLE_", ""))
            .collect(Collectors.toSet());
    }
    
    public Authentication authenticate(String email, String password) {
        try {
            return authenticationService.authenticate(email, password);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Error de autenticación: " + e.getMessage());
        }
    }
}
