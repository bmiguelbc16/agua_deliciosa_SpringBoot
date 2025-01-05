package com.bances.agua_deliciosa.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bances.agua_deliciosa.service.auth.AuthenticationService;
import com.bances.agua_deliciosa.service.auth.SecurityService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/verify")
public class VerificationController extends AuthController {
    
    private final AuthenticationService authService;
    
    public VerificationController(SecurityService securityService, AuthenticationService authService) {
        super(securityService);
        this.authService = authService;
    }
    
    @GetMapping("/email")
    public String verifyEmail(
        @RequestParam(required = false) String token,
        @RequestParam(required = false) String email,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if (token == null || token.isEmpty()) {
            setupCommonAttributes(model);
            return view("verify-email");
        }
        
        try {
            authService.verifyEmail(token, email);
            addSuccessMessage(redirectAttributes, "Email verificado exitosamente");
            return redirect("/login");
        } catch (Exception e) {
            return handleAuthError(e, redirectAttributes, "/verify/email");
        }
    }
}