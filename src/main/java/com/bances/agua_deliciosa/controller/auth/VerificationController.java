package com.bances.agua_deliciosa.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.bances.agua_deliciosa.controller.base.BaseController;
import com.bances.agua_deliciosa.service.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/auth/email")
@RequiredArgsConstructor
public class VerificationController extends BaseController {
    
    private final AuthenticationService authService;
    
    @GetMapping("/verify/{userId}/{token}")
    public String verifyEmail(
        @PathVariable Long userId,
        @PathVariable String token,
        RedirectAttributes redirectAttributes
    ) {
        try {
            authService.verifyEmail(userId, token);
            addSuccessMessage(redirectAttributes, "Email verificado exitosamente");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
        }
        return "redirect:/auth/login";
    }
    
    @Override
    protected String getViewPrefix() {
        return "auth";
    }
} 