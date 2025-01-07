package com.bances.agua_deliciosa.controller.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bances.agua_deliciosa.service.auth.SecurityService;

@Controller
public class LoginController extends AuthController {

    public LoginController(SecurityService securityService) {
        super(securityService);
    }

    @GetMapping("/login")
    public String showLoginForm(
        Authentication authentication,
        Model model,
        @RequestParam(required = false) String error,
        @RequestParam(required = false) String logout
    ) {
        if (authentication != null && authentication.isAuthenticated()) {
            // Si ya está autenticado, redirigir según el rol
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Client"))) {
                logAction("LOGIN_REDIRECT", "Redirigiendo a dashboard de cliente");
                return redirect("/client/dashboard");
            } else {
                // Si es Admin o Employee
                logAction("LOGIN_REDIRECT", "Redirigiendo a dashboard de admin");
                return redirect("/admin/dashboard");
            }
        }

        setupCommonAttributes(model);
        
        if (error != null) {
            model.addAttribute("error", "Credenciales inválidas");
            logAction("LOGIN_ERROR", "Intento de login fallido");
        }
        
        if (logout != null) {
            model.addAttribute("info", "Has cerrado sesión exitosamente");
            logAction("LOGOUT", "Usuario cerró sesión");
        }

        return view("login");
    }

    @GetMapping("/")
    public String root(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Client"))) {
                return "redirect:/client/dashboard";
            }
        }
        return "redirect:/admin/dashboard";
    }
}