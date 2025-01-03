package com.bances.agua_deliciosa.service.auth;

import com.bances.agua_deliciosa.exception.UnauthorizedAccessException;
import com.bances.agua_deliciosa.exception.InvalidPasswordException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final AuthenticationManager authenticationManager;
    private final CoreUserService userService;
    private final SecurityService securityService;
    
    // Método para autenticar al usuario con email y contraseña
    public void authenticate(String email, String password) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (BadCredentialsException e) {
            throw new UnauthorizedAccessException("Credenciales incorrectas", e);
        }
    }
    
    // Método para verificar email
    public void verifyEmail(Long userId) {
        userService.verifyEmail(userId);
    }
    
    // Método para restablecer la contraseña
    public void resetPassword(String email, String password) {
        userService.resetPassword(email, password);
    }
    
    // Método para enviar el enlace de restablecimiento de contraseña
    public void sendResetPasswordLink(String email) {
        userService.sendResetPasswordLink(email);
    }
    
    // Método para cambiar la contraseña
    public void confirmPassword(String currentPassword, String newPassword) {
        var user = securityService.getCurrentUser();
        if (user == null) {
            throw new UnauthorizedAccessException("Usuario no autenticado");
        }
        
        if (!userService.checkPassword(user, currentPassword)) {
            throw new InvalidPasswordException("La contraseña actual es incorrecta");
        }
        
        userService.updatePassword(user.getId(), newPassword);
    }
}
