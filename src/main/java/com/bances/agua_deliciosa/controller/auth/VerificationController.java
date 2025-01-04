package com.bances.agua_deliciosa.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import com.bances.agua_deliciosa.service.auth.AuthenticationService;
import com.bances.agua_deliciosa.service.auth.SecurityService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/auth/verify")
public class VerificationController extends AuthController {
    
    private final AuthenticationService authService;
    
    public VerificationController(SecurityService securityService, AuthenticationService authService) {
        super(securityService);
        this.authService = authService;
    }
    
    @GetMapping
    public String showVerificationForm(
        @RequestParam String token,
        @RequestParam String email,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        try {
            setupCommonAttributes(model);
            model.addAttribute("token", token);
            model.addAttribute("email", email);
            return view("verify-email");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, "El enlace ha expirado o no es válido");
            return "redirect:/login";
        }
    }
    
    @PostMapping
    public String verifyEmail(
        @RequestParam @Valid String email,
        @RequestParam @Valid String token,
        RedirectAttributes redirectAttributes
    ) {
        try {
            authService.verifyEmail(email, token);
            addSuccessMessage(redirectAttributes, "Email verificado exitosamente");
            return "redirect:/login";
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
            return "redirect:/auth/verify?token=" + token + "&email=" + email;
        }
    }
}