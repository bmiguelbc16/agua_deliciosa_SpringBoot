package com.bances.agua_deliciosa.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class LoginController {
    
    @GetMapping("/login")
    public String showLoginForm(Model model, 
                                @RequestParam(required = false) String error,
                                @RequestParam(required = false) String logout) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            return "redirect:/dashboard";
        }
        
        if (error != null) {
            model.addAttribute("error", "Credenciales inválidas. Por favor, intente nuevamente.");
        }
        
        if (logout != null) {
            model.addAttribute("message", "Has cerrado sesión exitosamente.");
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
            .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", "").toLowerCase())
            .orElse("cliente");
            
        log.info("Redirigiendo a /{}/dashboard para el rol: {}", role, role);
        
        return String.format("redirect:/%s/dashboard", role);
    }
} 