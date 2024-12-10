package com.bances.agua_deliciosa.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class LoginController {
    
    @GetMapping("/login")
    public String showLoginForm(Model model, 
                                @RequestParam(required = false) String error,
                                HttpServletRequest request) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            String role = auth.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> {
                    String roleStr = grantedAuthority.getAuthority().replace("ROLE_", "").toLowerCase();
                    return roleStr.equals("cliente") ? "client" : roleStr;
                })
                .orElse("client");
                
            return String.format("redirect:/%s/dashboard", role);
        }
        
        if (error != null) {
            model.addAttribute("error", "Credenciales inválidas");
        }
        
        if (request.getSession().getAttribute("showLogoutMessage") != null) {
            model.addAttribute("message", "Has cerrado sesión exitosamente");
            request.getSession().removeAttribute("showLogoutMessage");
        }
        
        return "auth/login";
    }
    
    @GetMapping("/dashboard")
    public String dashboard() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }
        
        String role = auth.getAuthorities().stream()
            .findFirst()
            .map(grantedAuthority -> {
                String roleStr = grantedAuthority.getAuthority().replace("ROLE_", "").toLowerCase();
                // Asegurarnos de que sea "client" y no "cliente"
                return roleStr.equals("cliente") ? "client" : roleStr;
            })
            .orElse("client");
            
        log.info("Redirigiendo a /{}/dashboard para el rol: {}", role, role);
        
        return String.format("redirect:/%s/dashboard", role);
    }
}