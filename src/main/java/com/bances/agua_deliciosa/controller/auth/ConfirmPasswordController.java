package com.bances.agua_deliciosa.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;

import com.bances.agua_deliciosa.service.auth.AuthenticationService;
import com.bances.agua_deliciosa.service.auth.SecurityService;
import com.bances.agua_deliciosa.dto.auth.ConfirmPasswordDTO;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/auth/password")
public class ConfirmPasswordController extends AuthController {
    
    private final AuthenticationService authService;
    
    public ConfirmPasswordController(SecurityService securityService, AuthenticationService authService) {
        super(securityService);
        this.authService = authService;
    }
    
    @GetMapping("/confirm")
    public String showConfirmForm(Model model) {
        setupCommonAttributes(model);
        model.addAttribute("confirmPasswordDTO", new ConfirmPasswordDTO());
        return view("confirm-password");
    }
    
    @PostMapping("/confirm")
    public String confirm(
        @Valid @ModelAttribute ConfirmPasswordDTO confirmPasswordDTO,
        BindingResult result,
        RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            return view("confirm-password");
        }
        
        try {
            authService.confirmPassword(
                confirmPasswordDTO.getCurrentPassword(),
                confirmPasswordDTO.getNewPassword()
            );
            addSuccessMessage(redirectAttributes, "Contraseña confirmada exitosamente");
            return "redirect:/dashboard";
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
            return "redirect:/auth/password/confirm";
        }
    }
    
    @Override
    protected String getViewPrefix() {
        return "auth";
    }
} 