package com.bances.agua_deliciosa.security.token;

import com.bances.agua_deliciosa.model.User;

public interface TokenService {
    String generatePasswordResetToken(User user);
    String generateVerificationToken(User user);
    boolean validateVerificationToken(String email, String token);
    boolean validateResetToken(String email, String token);
    void deleteExpiredTokens();
}
