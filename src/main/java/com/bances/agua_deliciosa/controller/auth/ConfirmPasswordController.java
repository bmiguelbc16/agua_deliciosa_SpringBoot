package com.bances.agua_deliciosa.controller.auth;

import com.bances.agua_deliciosa.service.auth.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth/password")
@RequiredArgsConstructor
public class ConfirmPasswordController {
    private final SecurityService securityService;

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPassword(@RequestParam String currentPassword, @RequestParam String newPassword) {
        var user = securityService.getUser();
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Usuario no autenticado"));
        }

        try {
            securityService.authenticate(user.getEmail(), currentPassword);
            // Aquí iría la lógica para cambiar la contraseña
            return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Contraseña actual incorrecta"));
        }
    }
}