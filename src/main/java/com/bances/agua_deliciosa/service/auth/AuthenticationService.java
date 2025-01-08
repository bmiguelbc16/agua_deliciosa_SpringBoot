package com.bances.agua_deliciosa.service.auth;

import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.model.UserToken;
import com.bances.agua_deliciosa.model.UserVerification;
import com.bances.agua_deliciosa.repository.UserRepository;
import com.bances.agua_deliciosa.repository.UserTokenRepository;
import com.bances.agua_deliciosa.repository.UserVerificationRepository;
import com.bances.agua_deliciosa.security.token.TokenService;
import com.bances.agua_deliciosa.service.core.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final UserVerificationRepository userVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final SecurityService securityService;

    public Authentication authenticate(String email, String password) {
        return securityService.authenticate(email, password);
    }

    public void verifyEmail(String token, String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        UserVerification verification = userVerificationRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Token de verificación inválido"));
        
        if (verification.isVerified()) {
            throw new RuntimeException("Email ya verificado");
        }
        
        if (verification.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El token de verificación ha expirado");
        }
        
        verification.setVerified(true);
        userVerificationRepository.save(verification);
        
        user.setEmailVerifiedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = tokenService.generatePasswordResetToken(user);
        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    public void resetPassword(String token, String newPassword) {
        Optional<UserToken> userToken = userTokenRepository.findByToken(token);
        if (userToken.isEmpty() || !tokenService.validateResetToken(token, "")) {
            throw new RuntimeException("Token inválido o expirado");
        }

        User user = userRepository.findById(userToken.get().getUserId())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        userTokenRepository.deleteById(userToken.get().getId());
    }
}
