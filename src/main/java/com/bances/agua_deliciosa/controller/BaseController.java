package com.bances.agua_deliciosa.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseController {
    
    /**
     * Agrega atributos comunes al modelo que serán necesarios en todas las vistas
     */
    protected void addCommonAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            model.addAttribute("currentUser", auth.getName());
            model.addAttribute("userRole", auth.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", ""))
                .orElse(""));
        }
    }

    /**
     * Registra una acción en el log
     */
    protected void logAction(String action) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "anonymous";
        log.info("User '{}' - {}", username, action);
    }

    /**
     * Verifica si el usuario actual tiene un rol específico
     */
    protected boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_" + role.toUpperCase()));
    }
} 