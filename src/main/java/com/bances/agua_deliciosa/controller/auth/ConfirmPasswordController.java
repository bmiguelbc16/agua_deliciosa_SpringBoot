package com.bances.agua_deliciosa.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.bances.agua_deliciosa.controller.base.BaseController;
import com.bances.agua_deliciosa.service.auth.AuthenticationService;
import com.bances.agua_deliciosa.dto.auth.ConfirmPasswordDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/auth/password")
@RequiredArgsConstructor
public class ConfirmPasswordController extends BaseController {
    
    private final AuthenticationService authService;
    
    @GetMapping("/confirm")
    public String showConfirmForm(Model model) {
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