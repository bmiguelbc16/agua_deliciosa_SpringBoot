package com.bances.agua_deliciosa.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bances.agua_deliciosa.service.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth/reset-password")
@RequiredArgsConstructor
public class ResetPasswordController {
    
    private final AuthenticationService authenticationService;
    
    @PostMapping("/request")
    public ResponseEntity<Void> requestReset(@RequestParam String email) {
        authenticationService.sendResetPasswordLink(email);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/reset")
    public ResponseEntity<Void> resetPassword(
        @RequestParam String email,
        @RequestParam String password
    ) {
        authenticationService.resetPassword(email, password);
        return ResponseEntity.ok().build();
    }
}