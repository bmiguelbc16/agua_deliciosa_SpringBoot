package com.bances.agua_deliciosa.controller.auth;

import com.bances.agua_deliciosa.service.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth/verify")
@RequiredArgsConstructor
public class VerificationController {
    private final AuthenticationService authService;

    protected String redirect(String path) {
        return "redirect:" + path;
    }

    protected void addSuccessMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("successMessage", message);
    }

    protected void addErrorMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("errorMessage", message);
    }

    @GetMapping
    public String verifyEmail(@RequestParam String token, @RequestParam String email, RedirectAttributes redirectAttributes) {
        try {
            authService.verifyEmail(token, email);
            addSuccessMessage(redirectAttributes, "Email verificado exitosamente");
            return redirect("/auth/login");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
            return redirect("/auth/login");
        }
    }
}