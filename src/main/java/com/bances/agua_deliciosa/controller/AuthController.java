package com.bances.agua_deliciosa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;

import java.util.Collections;

import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.service.UserService;
import com.bances.agua_deliciosa.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AuthController {
    
    private final UserService userService;
    private final RoleRepository roleRepository;
    
    public AuthController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }
    
    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("error", "Credenciales inválidas");
        }
        if (logout != null) {
            model.addAttribute("message", "Has cerrado sesión exitosamente");
        }
        return "auth/login";
    }
    
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        try {
            // Asignar rol CLIENTE por defecto
            Role clientRole = roleRepository.findByName("ROLE_CLIENTE")
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            user.setRoles(Collections.singleton(clientRole));
            
            userService.createUser(user);
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            log.error("Error al registrar usuario: {}", e.getMessage());
            return "redirect:/register?error=true";
        }
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Authentication auth) {
        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        } else if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_GERENTE"))) {
            return "redirect:/gerente/dashboard";
        }
        // ... otros roles
        return "redirect:/cliente/dashboard";
    }
} 