package com.bances.agua_deliciosa.service.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.bances.agua_deliciosa.model.User;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SecurityService {
    
    public User getCurrentUser() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) auth.getPrincipal();
                User user = new User();
                user.setName(userDetails.getUsername());
                user.setEmail(userDetails.getUsername());
                return user;
            }
            return null;
        } catch (Exception e) {
            log.error("Error getting current user", e);
            return null;
        }
    }
    
    public boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_" + role.toUpperCase()));
    }
    
    public boolean hasAnyRole(String... roles) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
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
    
    public Set<String> getCurrentUserRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return Set.of();
        
        return auth.getAuthorities().stream()
            .map(ga -> ga.getAuthority().replace("ROLE_", ""))
            .collect(Collectors.toSet());
    }
}
