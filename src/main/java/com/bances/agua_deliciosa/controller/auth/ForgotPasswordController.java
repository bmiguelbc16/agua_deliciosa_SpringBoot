package com.bances.agua_deliciosa.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bances.agua_deliciosa.dto.auth.ResetPasswordDTO;
import com.bances.agua_deliciosa.service.auth.AuthenticationService;
import com.bances.agua_deliciosa.service.auth.SecurityService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/forgot-password")
public class ForgotPasswordController extends AuthController {
    
    private final AuthenticationService authService;
    
    public ForgotPasswordController(SecurityService securityService, AuthenticationService authService) {
        super(securityService);
        this.authService = authService;
    }
    
    @GetMapping
    public String showForgotForm(Model model) {
        setupCommonAttributes(model);
        return view("forgot-password");
    }
    
    @PostMapping("/request")
    public String requestReset(
        @RequestParam @Valid String email,
        RedirectAttributes redirectAttributes
    ) {
        try {
            authService.initiatePasswordReset(email);
            addSuccessMessage(redirectAttributes, "Se ha enviado un enlace a tu correo");
            return redirect("/login");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
            return redirect("/forgot-password");
        }
    }
    
    @GetMapping("/reset")
    public String showResetForm(
        @RequestParam(required = false) String token,
        @RequestParam(required = false) String email,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if (token == null || token.isEmpty() || email == null || email.isEmpty()) {
            setupCommonAttributes(model);
            model.addAttribute("resetPasswordDTO", new ResetPasswordDTO());
            return view("reset-password");
        }
        
        try {
            setupCommonAttributes(model);
            model.addAttribute("resetPasswordDTO", new ResetPasswordDTO());
            model.addAttribute("token", token);
            model.addAttribute("email", email);
            return view("reset-password");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, "El enlace ha expirado o no es válido");
            return redirect("/forgot-password");
        }
    }
    
    @PostMapping("/reset")
    public String resetPassword(@Valid ResetPasswordDTO resetPasswordDTO, RedirectAttributes redirectAttributes) {
        try {
            authService.resetPassword(
                resetPasswordDTO.getToken(),
                resetPasswordDTO.getPassword(),
                resetPasswordDTO.getPassword()
            );
            addSuccessMessage(redirectAttributes, "Contraseña restablecida exitosamente");
            return redirect("/login");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
            return redirect("/forgot-password");
        }
    }
    
    @Override
    protected String getViewPrefix() {
        return "auth";
    }
}