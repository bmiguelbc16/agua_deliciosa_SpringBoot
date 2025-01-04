package com.bances.agua_deliciosa.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bances.agua_deliciosa.service.auth.AuthenticationService;
import com.bances.agua_deliciosa.service.auth.SecurityService;
import com.bances.agua_deliciosa.dto.auth.ResetPasswordDTO;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/auth/reset-password")
public class ResetPasswordController extends AuthController {
    
    private final AuthenticationService authService;
    
    public ResetPasswordController(SecurityService securityService, AuthenticationService authService) {
        super(securityService);
        this.authService = authService;
    }
    
    @PostMapping("/request")
    public String requestReset(
        @RequestParam @Valid String email,
        RedirectAttributes redirectAttributes
    ) {
        try {
            authService.initiatePasswordReset(email);
            addSuccessMessage(redirectAttributes, "Se ha enviado un enlace a tu correo");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
        }
        return "redirect:/auth/password/forgot";
    }
    
    @PostMapping("/reset")
    public String resetPassword(
        @Valid @ModelAttribute ResetPasswordDTO resetPasswordDTO,
        RedirectAttributes redirectAttributes
    ) {
        try {
            authService.resetPassword(
                resetPasswordDTO.getEmail(),
                resetPasswordDTO.getToken(),
                resetPasswordDTO.getPassword()
            );
            addSuccessMessage(redirectAttributes, "Contraseña actualizada exitosamente");
            return "redirect:/login";
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
            return "redirect:/auth/password/forgot";
        }
    }
}