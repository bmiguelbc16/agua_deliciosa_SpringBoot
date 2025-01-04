package com.bances.agua_deliciosa.controller.auth;

import com.bances.agua_deliciosa.controller.base.BaseController;
import com.bances.agua_deliciosa.service.auth.SecurityService;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AuthController extends BaseController {
    
    protected final SecurityService securityService;
    
    protected void setupCommonAttributes(Model model) {
        Authentication auth = securityService.getCurrentAuthentication();
        model.addAttribute("currentUser", securityService.getCurrentUser());
        model.addAttribute("isAuthenticated", auth != null && auth.isAuthenticated());
        model.addAttribute("userRoles", securityService.getCurrentUserRoles());
    }

    protected String handleAuthError(Exception e, RedirectAttributes redirectAttributes, String defaultRedirect) {
        String errorMessage = e.getMessage();
        if (errorMessage == null || errorMessage.isEmpty()) {
            errorMessage = "Ha ocurrido un error de autenticación";
        }
        addErrorMessage(redirectAttributes, errorMessage);
        logAction("AUTH_ERROR", errorMessage);
        return redirect(defaultRedirect);
    }

    protected boolean isEmailVerified(String email) {
        return securityService.isEmailVerified(email);
    }

    protected boolean isPasswordValid(String password) {
        return password != null && 
               password.length() >= 8 && 
               password.matches(".*[A-Z].*") && 
               password.matches(".*[a-z].*") && 
               password.matches(".*\\d.*");
    }

    protected void validatePassword(String password, RedirectAttributes redirectAttributes) {
        if (!isPasswordValid(password)) {
            throw new IllegalArgumentException(
                "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número"
            );
        }
    }

    @Override
    protected String getViewPrefix() {
        return "auth";
    }
}
