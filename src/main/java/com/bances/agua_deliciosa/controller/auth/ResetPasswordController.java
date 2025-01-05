package com.bances.agua_deliciosa.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bances.agua_deliciosa.dto.auth.ResetPasswordDTO;
import com.bances.agua_deliciosa.service.auth.AuthenticationService;
import com.bances.agua_deliciosa.service.auth.SecurityService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/reset-password")
public class ResetPasswordController extends AuthController {
    
    private final AuthenticationService authService;
    
    public ResetPasswordController(SecurityService securityService, AuthenticationService authService) {
        super(securityService);
        this.authService = authService;
    }
    
    @GetMapping
    public String showResetForm(Model model) {
        model.addAttribute("resetPasswordDTO", new ResetPasswordDTO());
        return view("reset-password");
    }
    
    @PostMapping
    public String resetPassword(@Valid ResetPasswordDTO resetPasswordDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            addErrorMessage(redirectAttributes, "Por favor, corrija los errores en el formulario");
            return redirect("/reset-password");
        }
        
        try {
            authService.resetPassword(
                resetPasswordDTO.getToken(),
                resetPasswordDTO.getPassword(),
                resetPasswordDTO.getPassword() // Confirmación igual a password
            );
            addSuccessMessage(redirectAttributes, "Contraseña restablecida exitosamente");
            return redirect("/login");
        } catch (Exception e) {
            return handleAuthError(e, redirectAttributes, "/reset-password");
        }
    }
}