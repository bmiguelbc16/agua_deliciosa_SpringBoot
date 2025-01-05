package com.bances.agua_deliciosa.service.auth;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bances.agua_deliciosa.dto.auth.*;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.model.UserVerification;
import com.bances.agua_deliciosa.model.VerificationType;
import com.bances.agua_deliciosa.repository.UserRepository;
import com.bances.agua_deliciosa.repository.UserVerificationRepository;
import com.bances.agua_deliciosa.service.core.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final UserRepository userRepository;
    private final UserVerificationRepository verificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return AuthenticationResponse.builder()
            .token(generateToken(user))
            .build();
    }

    @Transactional
    public void verifyEmail(String token, String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UserVerification verification = verificationRepository.findByTokenAndVerificationType(token, VerificationType.EMAIL_VERIFICATION)
            .orElseThrow(() -> new RuntimeException("Token de verificación inválido"));

        if (!verification.getUser().equals(user)) {
            throw new RuntimeException("Token no corresponde al usuario");
        }

        if (verification.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El token de verificación ha expirado");
        }

        if (verification.isVerified()) {
            throw new RuntimeException("Este email ya ha sido verificado");
        }

        verification.setVerified(true);
        verification.setVerifiedAt(LocalDateTime.now());
        verificationRepository.save(verification);

        emailService.sendEmailVerifiedNotification(email);
    }

    @Transactional
    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UserVerification verification = verificationRepository.findByUserAndVerificationType(user, VerificationType.PASSWORD_RESET)
            .map(v -> {
                v.setToken(generateVerificationToken());
                v.setExpiryDate(LocalDateTime.now().plusHours(24));
                v.setVerified(false);
                v.setVerifiedAt(null);
                return v;
            })
            .orElseGet(() -> UserVerification.builder()
                .user(user)
                .token(generateVerificationToken())
                .verificationType(VerificationType.PASSWORD_RESET)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build());

        verificationRepository.save(verification);
        emailService.sendPasswordResetEmail(email, "http://localhost:8080/reset-password?token=" + verification.getToken());
    }

    @Transactional
    public void resetPassword(String token, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Las contraseñas no coinciden");
        }

        UserVerification verification = verificationRepository.findByTokenAndVerificationType(token, VerificationType.PASSWORD_RESET)
            .orElseThrow(() -> new RuntimeException("Token de restablecimiento inválido"));

        if (verification.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El token de restablecimiento ha expirado");
        }

        if (verification.isVerified()) {
            throw new RuntimeException("Este token ya ha sido utilizado");
        }

        User user = verification.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        verification.setVerified(true);
        verification.setVerifiedAt(LocalDateTime.now());
        verificationRepository.save(verification);

        emailService.sendPasswordChangedEmail(user.getEmail());
    }

    @Transactional
    public void confirmPassword(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }
    }

    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }

    private String generateToken(User user) {
        return "jwt_token_" + user.getId() + "_" + System.currentTimeMillis();
    }
}
