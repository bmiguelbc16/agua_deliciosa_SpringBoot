package com.bances.agua_deliciosa.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.bances.agua_deliciosa.service.UserService;
import com.bances.agua_deliciosa.controller.BaseController;

@Controller
@RequestMapping("/email")
public class VerificationController extends BaseController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/verify/{id}/{hash}")
    public String verify(@PathVariable Long id, @PathVariable String hash) {
        try {
            userService.verifyEmail(id, hash);
            return "redirect:/login?verified=1";
        } catch (Exception e) {
            return "redirect:/login?verified=0";
        }
    }
    
    @GetMapping("/verify/resend")
    public String resend() {
        try {
            userService.resendVerificationEmail();
            return "redirect:/login?resent=1";
        } catch (Exception e) {
            return "redirect:/login?resent=0";
        }
    }
} 