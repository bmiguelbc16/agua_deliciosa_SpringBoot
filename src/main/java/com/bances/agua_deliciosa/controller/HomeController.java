package com.bances.agua_deliciosa.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String index(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            // Redirigir según el rol
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Admin"))) {
                return "redirect:/admin/dashboard";
            }
            // Aquí puedes agregar más redirecciones según otros roles
            return "redirect:/admin/dashboard"; // Por defecto
        }
        return "redirect:/login";
    }
}