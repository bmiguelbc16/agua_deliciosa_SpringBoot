package com.bances.agua_deliciosa.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.bances.agua_deliciosa.service.UserService;
import com.bances.agua_deliciosa.controller.BaseController;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/password")
public class ResetPasswordController extends BaseController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/reset/{token}")
    public String showResetForm(@PathVariable String token, Model model) {
        if (!userService.isValidResetToken(token)) {
            return "redirect:/password/forgot?invalid";
        }
        
        model.addAttribute("token", token);
        return "auth/passwords/reset";
    }
    
    @PostMapping("/reset")
    public String reset(@Valid @RequestParam String token,
                        @Valid @RequestParam String email,
                        @Valid @RequestParam String password,
                        @Valid @RequestParam String password_confirmation,
                        Model model) {
        
        if (!password.equals(password_confirmation)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "auth/passwords/reset";
        }
        
        try {
            userService.resetPassword(token, email, password);
            return "redirect:/login?reset=success";
        } catch (Exception e) {
            model.addAttribute("error", "No pudimos restablecer tu contraseña. Por favor intenta nuevamente.");
            return "auth/passwords/reset";
        }
    }
} 