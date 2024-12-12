package com.bances.agua_deliciosa.service.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final AuthenticationManager authenticationManager;
    private final CoreUserService userService;
    private final SecurityService securityService;
    
    public void authenticate(String email, String password) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
    
    public void verifyEmail(Long userId, String hash) {
        userService.verifyEmail(userId, hash);
    }
    
    public void resetPassword(String token, String email, String password) {
        userService.resetPassword(token, email, password);
    }
    
    public void sendResetPasswordLink(String email) {
        userService.sendResetPasswordLink(email);
    }
    
    public void confirmPassword(String currentPassword, String newPassword) {
        // Obtener el usuario actual
        var user = securityService.getCurrentUser();
        if (user == null) {
            throw new RuntimeException("Usuario no autenticado");
        }
        
        // Verificar la contraseña actual
        if (!userService.checkPassword(user, currentPassword)) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }
        
        // Actualizar la contraseña
        userService.updatePassword(user.getId(), newPassword);
    }
} 