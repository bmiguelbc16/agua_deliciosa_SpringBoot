package com.bances.agua_deliciosa.controller.auth;

import com.bances.agua_deliciosa.service.auth.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    protected final SecurityService securityService;

    protected void addSuccessMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("successMessage", message);
    }

    protected void addErrorMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("errorMessage", message);
    }

    protected String redirect(String path) {
        return "redirect:" + path;
    }

    protected String view(String viewName) {
        return "auth/" + viewName;
    }

    protected void setupCommonAttributes(Model model) {
        Authentication auth = securityService.getCurrentAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            model.addAttribute("currentUser", securityService.getUser());
            model.addAttribute("roles", auth.getAuthorities());
        }
    }

    protected String handleAuthError(Exception e, RedirectAttributes redirectAttributes, String defaultRedirect) {
        String errorMessage = e.getMessage();
        if (errorMessage == null || errorMessage.isEmpty()) {
            errorMessage = "Ha ocurrido un error de autenticación";
        }
        addErrorMessage(redirectAttributes, errorMessage);
        return redirect(defaultRedirect);
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        if (securityService.isAuthenticated()) {
            return redirect("/");
        }
        setupCommonAttributes(model);
        return view("login");
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email, 
            @RequestParam String password, 
            RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = securityService.authenticate(email, password);
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return redirect("/admin/dashboard");
            } else {
                return redirect("/dashboard");
            }
        } catch (Exception e) {
            return handleAuthError(e, redirectAttributes, "/auth/login");
        }
    }

    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        try {
            // El logout real lo maneja Spring Security
            addSuccessMessage(redirectAttributes, "Has cerrado sesión exitosamente");
            return redirect("/auth/login");
        } catch (Exception e) {
            return handleAuthError(e, redirectAttributes, "/");
        }
    }
}
