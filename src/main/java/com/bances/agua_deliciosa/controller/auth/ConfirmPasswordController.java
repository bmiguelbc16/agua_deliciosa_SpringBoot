package com.bances.agua_deliciosa.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bances.agua_deliciosa.dto.auth.ConfirmPasswordDTO;
import com.bances.agua_deliciosa.service.auth.AuthenticationService;
import com.bances.agua_deliciosa.service.auth.SecurityService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/confirm-password")
public class ConfirmPasswordController extends AuthController {
    
    private final AuthenticationService authService;
    
    public ConfirmPasswordController(SecurityService securityService, AuthenticationService authService) {
        super(securityService);
        this.authService = authService;
    }
    
    @GetMapping
    public String showConfirmForm(Model model) {
        setupCommonAttributes(model);
        model.addAttribute("confirmPasswordDTO", new ConfirmPasswordDTO());
        return view("confirm-password");
    }
    
    @PostMapping
    public String confirmPassword(@Valid ConfirmPasswordDTO confirmPasswordDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return view("confirm-password");
        }
        
        try {
            authService.confirmPassword(
                confirmPasswordDTO.getCurrentPassword(),
                confirmPasswordDTO.getNewPassword()
            );
            addSuccessMessage(redirectAttributes, "Contraseña confirmada exitosamente");
            return redirect("/login");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
            return redirect("/confirm-password");
        }
    }
    
    @Override
    protected String getViewPrefix() {
        return "auth";
    }
}