package com.bances.agua_deliciosa.controller.auth;

import com.bances.agua_deliciosa.service.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth/password")
@RequiredArgsConstructor
public class ResetPasswordController {
    private final AuthenticationService authService;

    @PostMapping("/reset/{token}")
    public ResponseEntity<?> resetPassword(@PathVariable String token, @RequestParam String password, @RequestParam String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body(Map.of("message", "La contraseña y la confirmación no coinciden"));
        }
        try {
            authService.resetPassword(token, password);
            return ResponseEntity.ok(Map.of("message", "Contraseña restablecida correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}