package com.bances.agua_deliciosa.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;

import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.RoleRepository;
import com.bances.agua_deliciosa.repository.UserRepository;
import com.bances.agua_deliciosa.security.token.TokenService;
import com.bances.agua_deliciosa.service.EmailService;

import java.time.LocalDateTime;

@Service
public class CoreUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    @Qualifier("notificationEmailService")
    private EmailService emailService;

    @Transactional
    public User registerUser(User user) {
        // Verificar si el email ya existe
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Verificar si el documento ya existe
        if (user.getDocumentNumber() != null && userRepository.existsByDocumentNumber(user.getDocumentNumber())) {
            throw new RuntimeException("El número de documento ya está registrado");
        }

        // Asignar rol por defecto (ROLE_CLIENT)
        Role defaultRole = roleRepository.findByName("ROLE_CLIENT")
                .orElseThrow(() -> new RuntimeException("Rol por defecto no encontrado"));
        user.setRole(defaultRole);

        // Encriptar contraseña
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Guardar usuario
        user = userRepository.save(user);

        // Generar token de verificación y enviar email
        String verificationToken = tokenService.generateVerificationToken(user);
        String verificationLink = generateVerificationLink(verificationToken);
        emailService.sendWelcomeEmail(user.getEmail(), user.getName(), verificationLink);

        return user;
    }

    @Transactional
    public void verifyEmail(String email, String token) {
        if (!tokenService.validateVerificationToken(email, token)) {
            throw new RuntimeException("Token de verificación inválido o expirado");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setEmailVerifiedAt(LocalDateTime.now());
        userRepository.save(user);
        emailService.sendEmailVerifiedNotification(email);
    }

    @Transactional
    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String resetToken = tokenService.generatePasswordResetToken(user);
        String resetLink = generatePasswordResetLink(resetToken);
        emailService.sendResetPasswordLink(email, resetLink);
    }

    @Transactional
    public void resetPassword(String email, String token, String newPassword) {
        if (!tokenService.validateResetToken(email, token)) {
            throw new RuntimeException("Token de restablecimiento inválido o expirado");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

    }
    

    private String generateVerificationLink(String token) {
        
        return "http://localhost:8080/api/v1/auth/verify-email?token=" + token;
    }
    

    private String generatePasswordResetLink(String token) {
        
        return "http://localhost:8080/api/v1/auth/reset-password?token=" + token;
    }
}