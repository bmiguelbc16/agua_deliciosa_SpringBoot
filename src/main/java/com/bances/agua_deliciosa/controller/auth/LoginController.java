package com.bances.agua_deliciosa.controller.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bances.agua_deliciosa.service.auth.SecurityService;
import com.bances.agua_deliciosa.util.Routes;

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
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                logAction("LOGIN_REDIRECT", "Redirigiendo a dashboard de admin");
                return redirect(Routes.Admin.DASHBOARD);
            } 
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLIENT"))) {
                logAction("LOGIN_REDIRECT", "Redirigiendo a dashboard de cliente");
                return redirect(Routes.Client.DASHBOARD);
            }
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))) {
                logAction("LOGIN_REDIRECT", "Redirigiendo a dashboard de empleado");
                return redirect(Routes.Admin.DASHBOARD);
            }
            
            logAction("LOGIN_REDIRECT", "Usuario sin rol específico, redirigiendo a home");
            return redirect("/");
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
}