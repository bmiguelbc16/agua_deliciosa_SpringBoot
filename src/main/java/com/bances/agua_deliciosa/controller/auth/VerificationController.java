package com.bances.agua_deliciosa.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bances.agua_deliciosa.service.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth/verify")
@RequiredArgsConstructor
public class VerificationController {
    
    private final AuthenticationService authenticationService;
    
    @PostMapping("/{userId}")
    public ResponseEntity<Void> verifyEmail(@PathVariable Long userId) {
        authenticationService.verifyEmail(userId);
        return ResponseEntity.ok().build();
    }
}