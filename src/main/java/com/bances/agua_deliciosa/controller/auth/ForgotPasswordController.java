package com.bances.agua_deliciosa.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.bances.agua_deliciosa.service.UserService;
import com.bances.agua_deliciosa.controller.BaseController;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/password")
public class ForgotPasswordController extends BaseController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/forgot")
    public String showLinkRequestForm(Model model) {
        addCommonAttributes(model);
        return "auth/passwords/email";
    }
    
    @PostMapping("/email")
    public String sendResetLinkEmail(@Valid @RequestParam String email, Model model) {
        try {
            userService.sendResetLinkEmail(email);
            return "redirect:/password/forgot?status=sent";
        } catch (Exception e) {
            model.addAttribute("error", "No pudimos encontrar un usuario con esa dirección de correo electrónico.");
            return "auth/passwords/email";
        }
    }
} 