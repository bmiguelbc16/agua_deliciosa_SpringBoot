package com.bances.agua_deliciosa.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.bances.agua_deliciosa.controller.base.BaseController;
import com.bances.agua_deliciosa.service.auth.AuthenticationService;
import com.bances.agua_deliciosa.dto.auth.LoginDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller("authLoginController")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController extends BaseController {
    
    private final AuthenticationService authService;
    
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "auth/login";
    }
    
    @PostMapping("/auth/login")
    public String login(
        @Valid @ModelAttribute LoginDTO loginDTO,
        BindingResult result,
        RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            return "auth/login";
        }
        
        try {
            authService.authenticate(loginDTO.getEmail(), loginDTO.getPassword());
            // La redirección la maneja el AuthenticationSuccessHandler
            return null;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Credenciales inválidas");
            return "redirect:/login";
        }
    }
    
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/auth/login?logout";
    }
    
    @Override
    protected String getViewPrefix() {
        return "auth";
    }
}