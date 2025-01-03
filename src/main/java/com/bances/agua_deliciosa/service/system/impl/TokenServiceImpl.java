// package com.bances.agua_deliciosa.service.system.impl;

// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
// import com.bances.agua_deliciosa.model.User;
// import com.bances.agua_deliciosa.service.system.TokenService;
// import com.bances.agua_deliciosa.repository.UserRepository;
// import java.security.SecureRandom;
// import java.time.LocalDateTime;
// import java.util.Base64;
// import lombok.RequiredArgsConstructor;

// @Service
// @Transactional
// @RequiredArgsConstructor
// public class TokenServiceImpl implements TokenService {
    
//     private static final int TOKEN_LENGTH = 32;
//     private static final long TOKEN_VALIDITY_HOURS = 24;
//     private static final SecureRandom SECURE_RANDOM = new SecureRandom();
//     private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();
    
//     private final UserRepository userRepository;
    
//     @Override
//     public String generatePasswordResetToken(User user) {
//         String token = generateSecureToken();
//         user.setResetToken(token);
//         user.setResetTokenCreatedAt(LocalDateTime.now());
//         userRepository.save(user);
//         return token;
//     }
    
//     @Override
//     public boolean validateVerificationToken(User user, String token) {
//         return validateToken(
//             token,
//             user.getVerificationToken(),
//             user.getVerificationTokenCreatedAt()
//         );
//     }
    
//     @Override
//     public boolean validateResetToken(User user, String token) {
//         return validateToken(
//             token,
//             user.getResetToken(),
//             user.getResetTokenCreatedAt()
//         );
//     }
    
//     private String generateSecureToken() {
//         byte[] tokenBytes = new byte[TOKEN_LENGTH];
//         SECURE_RANDOM.nextBytes(tokenBytes);
//         return ENCODER.encodeToString(tokenBytes);
//     }
    
//     private boolean validateToken(String inputToken, String storedToken, LocalDateTime createdAt) {
//         if (storedToken == null || !storedToken.equals(inputToken)) {
//             return false;
//         }
        
//         return createdAt != null && 
//             createdAt.plusHours(TOKEN_VALIDITY_HOURS).isAfter(LocalDateTime.now());
//     }
    
//     public String generateVerificationToken(User user) {
//         String token = generateSecureToken();
//         user.setVerificationToken(token);
//         user.setVerificationTokenCreatedAt(LocalDateTime.now());
//         userRepository.save(user);
//         return token;
//     }
    
//     public void clearResetToken(User user) {
//         user.setResetToken(null);
//         user.setResetTokenCreatedAt(null);
//         userRepository.save(user);
//     }
    
//     public void clearVerificationToken(User user) {
//         user.setVerificationToken(null);
//         user.setVerificationTokenCreatedAt(null);
//         userRepository.save(user);
//     }
// } 