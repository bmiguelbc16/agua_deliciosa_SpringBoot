package com.bances.agua_deliciosa.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bances.agua_deliciosa.service.auth.AuthenticationService;
import com.bances.agua_deliciosa.service.auth.SecurityService;
import com.bances.agua_deliciosa.dto.auth.ResetPasswordDTO;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/auth/password")
public class ForgotPasswordController extends AuthController {
    
    private final AuthenticationService authService;
    
    public ForgotPasswordController(SecurityService securityService, AuthenticationService authService) {
        super(securityService);
        this.authService = authService;
    }
    
    @GetMapping("/forgot")
    public String showForgotForm(Model model) {
        setupCommonAttributes(model);
        return view("forgot-password");
    }
    
    @PostMapping("/forgot")
    public String sendResetLink(
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

    @GetMapping("/reset")
    public String showResetForm(
        @RequestParam String token,
        @RequestParam String email,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        try {
            setupCommonAttributes(model);
            ResetPasswordDTO resetPasswordDTO = new ResetPasswordDTO();
            resetPasswordDTO.setToken(token);
            resetPasswordDTO.setEmail(email);
            model.addAttribute("resetPasswordDTO", resetPasswordDTO);
            return view("reset-password");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, "El enlace ha expirado o no es válido");
            return "redirect:/auth/password/forgot";
        }
    }

    @PostMapping("/reset")
    public String resetPassword(
        @Valid @ModelAttribute ResetPasswordDTO resetPasswordDTO,
        BindingResult result,
        RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            return view("reset-password");
        }

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