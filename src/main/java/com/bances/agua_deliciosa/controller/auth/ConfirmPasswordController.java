package com.bances.agua_deliciosa.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.bances.agua_deliciosa.service.UserService;
import com.bances.agua_deliciosa.controller.BaseController;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

@Controller
@RequestMapping("/password")
public class ConfirmPasswordController extends BaseController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/confirm")
    public String showConfirmForm(Model model) {
        addCommonAttributes(model);
        return "auth/passwords/confirm";
    }
    
    @PostMapping("/confirm")
    public String confirm(@Valid @RequestParam String password, 
                            Authentication auth,
                            Model model) {
        try {
            if (userService.confirmPassword(auth.getName(), password)) {
                return "redirect:/home";
            }
            model.addAttribute("error", "La contraseña es incorrecta");
            return "auth/passwords/confirm";
        } catch (Exception e) {
            model.addAttribute("error", "Error al confirmar la contraseña");
            return "auth/passwords/confirm";
        }
    }
} 