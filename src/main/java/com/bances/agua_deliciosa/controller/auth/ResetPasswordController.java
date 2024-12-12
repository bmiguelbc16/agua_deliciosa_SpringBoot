package com.bances.agua_deliciosa.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.bances.agua_deliciosa.controller.base.BaseController;
import com.bances.agua_deliciosa.service.auth.AuthenticationService;
import com.bances.agua_deliciosa.dto.auth.ResetPasswordDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/auth/password")
@RequiredArgsConstructor
public class ResetPasswordController extends BaseController {
    
    private final AuthenticationService authService;
    
    @GetMapping("/reset/{token}")
    public String showResetForm(
        @PathVariable String token,
        Model model
    ) {
        model.addAttribute("token", token);
        model.addAttribute("resetPasswordDTO", new ResetPasswordDTO());
        return view("reset-password");
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
                resetPasswordDTO.getToken(),
                resetPasswordDTO.getEmail(),
                resetPasswordDTO.getPassword()
            );
            addSuccessMessage(redirectAttributes, "Contraseña actualizada exitosamente");
            return "redirect:/auth/login";
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
            return "redirect:/auth/password/reset/" + resetPasswordDTO.getToken();
        }
    }
    
    @Override
    protected String getViewPrefix() {
        return "auth";
    }
} 