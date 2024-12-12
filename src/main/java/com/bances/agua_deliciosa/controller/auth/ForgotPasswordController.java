package com.bances.agua_deliciosa.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.bances.agua_deliciosa.controller.base.BaseController;
import com.bances.agua_deliciosa.service.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;

@Controller("authForgotPasswordController")
@RequestMapping("/auth/password")
@RequiredArgsConstructor
public class ForgotPasswordController extends BaseController {
    
    private final AuthenticationService authService;
    
    @GetMapping("/forgot")
    public String showForgotForm() {
        return view("forgot-password");
    }
    
    @PostMapping("/forgot")
    public String sendResetLink(
        @RequestParam String email,
        RedirectAttributes redirectAttributes
    ) {
        try {
            authService.sendResetPasswordLink(email);
            addSuccessMessage(redirectAttributes, "Se ha enviado un enlace a tu correo");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
        }
        return "redirect:/auth/password/forgot";
    }
    
    @Override
    protected String getViewPrefix() {
        return "auth";
    }
} 