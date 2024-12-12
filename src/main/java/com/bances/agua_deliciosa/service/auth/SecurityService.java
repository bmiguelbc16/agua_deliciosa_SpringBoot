package com.bances.agua_deliciosa.service.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.bances.agua_deliciosa.model.User;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityService {
    
    /**
     * Obtiene el usuario autenticado actual
     */
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User) {
            return (User) auth.getPrincipal();
        }
        return null;
    }
    
    /**
     * Verifica si el usuario actual tiene un rol específico
     */
    public boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_" + role.toUpperCase()));
    }
    
    /**
     * Verifica si el usuario actual tiene alguno de los roles especificados
     */
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
    
    /**
     * Verifica si el usuario está autenticado
     */
    public boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated();
    }
    
    /**
     * Verifica si el usuario actual es un administrador
     */
    public boolean isAdmin() {
        return hasRole("ADMIN");
    }
    
    /**
     * Verifica si el usuario actual es un cliente
     */
    public boolean isClient() {
        return hasRole("CLIENTE");
    }
    
    /**
     * Verifica si el usuario actual tiene acceso a un recurso específico
     */
    public boolean hasAccess(String userType, Long resourceId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) return false;
        
        // Si es admin, tiene acceso a todo
        if (isAdmin()) return true;
        
        // Verifica si el recurso pertenece al usuario actual
        return currentUser.getUserableType().equals(userType) && 
                currentUser.getUserableId().equals(resourceId);
    }
    
    /**
     * Obtiene los roles del usuario actual
     */
    public Set<String> getCurrentUserRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return Set.of();
        
        return auth.getAuthorities().stream()
            .map(ga -> ga.getAuthority().replace("ROLE_", ""))
            .collect(Collectors.toSet());
    }
    
    public boolean isResourceOwner(String userType, Long resourceId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        
        return currentUser.getUserableType().equals(userType) && 
               currentUser.getUserableId().equals(resourceId);
    }
}
