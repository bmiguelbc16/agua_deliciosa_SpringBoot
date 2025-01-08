package com.bances.agua_deliciosa.controller.auth;

import com.bances.agua_deliciosa.service.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/auth/password")
@RequiredArgsConstructor
public class ForgotPasswordController {
    private final AuthenticationService authService;

    protected String redirect(String path) {
        return "redirect:" + path;
    }

    protected String view(String viewName) {
        return "auth/" + viewName;
    }

    protected void addSuccessMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("successMessage", message);
    }

    protected void addErrorMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("errorMessage", message);
    }

    @GetMapping("/forgot")
    public String showForgotForm(Model model) {
        return view("forgot-password");
    }

    @PostMapping("/forgot")
    public String requestReset(@RequestParam String email, RedirectAttributes redirectAttributes) {
        try {
            authService.initiatePasswordReset(email);
            addSuccessMessage(redirectAttributes, "Se ha enviado un correo con las instrucciones");
            return redirect("/auth/login");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
            return redirect("/auth/password/forgot");
        }
    }

    @GetMapping("/reset")
    public String showResetForm(@RequestParam String token, Model model) {
        try {
            model.addAttribute("token", token);
            return view("reset-password");
        } catch (Exception e) {
            return redirect("/auth/password/forgot");
        }
    }

    @PostMapping("/reset")
    public String resetPassword(@RequestParam String token, @RequestParam String password, RedirectAttributes redirectAttributes) {
        try {
            authService.resetPassword(token, password);
            addSuccessMessage(redirectAttributes, "Contraseña restablecida exitosamente");
            return redirect("/auth/login");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
            return redirect("/auth/password/reset?token=" + token);
        }
    }
}