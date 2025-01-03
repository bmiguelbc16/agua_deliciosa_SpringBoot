package com.bances.agua_deliciosa.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        // Redirigir según el rol
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_Admin"))) {
            return "redirect:/admin/dashboard";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_Cliente"))) {
            return "redirect:/client/dashboard";
        }

        return "redirect:/login";
    }
} 