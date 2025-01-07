package com.bances.agua_deliciosa.service.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.model.UserVerification;
import com.bances.agua_deliciosa.repository.UserRepository;
import com.bances.agua_deliciosa.service.core.JwtService;
import com.bances.agua_deliciosa.security.UserDetailsAdapter;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public String authenticate(String email, String password) {
        // Validar credenciales
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                email,
                password
            )
        );

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetailsAdapter userDetails = new UserDetailsAdapter(user);
        return jwtService.generateToken(userDetails);
    }

    public void verifyEmail(String token, String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
        UserVerification verification = user.getVerifications().stream()
            .filter(v -> v.isActive() && token.equals(v.getToken()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (verification.getVerifiedAt() != null) {
            throw new RuntimeException("Email already verified");
        }

        verification.setVerifiedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    
    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
        // Generar token único para reset de contraseña
        String resetToken = generateResetToken();
        
        // Guardar el token en la base de datos
        saveResetToken(user, resetToken);
        
        // Aquí deberías enviar el email con el token
        sendResetPasswordEmail(user, resetToken);
    }
    
    public void resetPassword(String token, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Las contraseñas no coinciden");
        }
        
        // Buscar usuario por token de reset
        User user = findUserByResetToken(token)
            .orElseThrow(() -> new RuntimeException("Token inválido o expirado"));
            
        // Actualizar contraseña
        user.setPassword(newPassword); // Asumiendo que hay un encoder configurado
        userRepository.save(user);
        
        // Invalidar token de reset
        invalidateResetToken(token);
    }
    
    public void confirmPassword(String currentPassword, String newPassword) {
        // Obtener usuario autenticado
        User user = getCurrentAuthenticatedUser();
        
        // Verificar contraseña actual
        if (!isPasswordValid(user, currentPassword)) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }
        
        // Actualizar contraseña
        user.setPassword(newPassword); // Asumiendo que hay un encoder configurado
        userRepository.save(user);
    }
    
    // Métodos auxiliares privados
    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }
    
    private void saveResetToken(User user, String token) {
        // Implementar lógica para guardar el token
        // Esto dependerá de tu modelo de datos
    }
    
    private void sendResetPasswordEmail(User user, String token) {
        // Implementar lógica para enviar email
        // Esto dependerá de tu configuración de email
    }
    
    private Optional<User> findUserByResetToken(String token) {
        // Implementar lógica para buscar usuario por token
        // Esto dependerá de tu modelo de datos
        return Optional.empty(); // Temporal
    }
    
    private void invalidateResetToken(String token) {
        // Implementar lógica para invalidar token
        // Esto dependerá de tu modelo de datos
    }
    
    private User getCurrentAuthenticatedUser() {
        // Obtener email del usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    
    private boolean isPasswordValid(User user, String password) {
        // Implementar validación de contraseña
        // Esto dependerá de tu configuración de seguridad
        return true; // Temporal
    }
}
