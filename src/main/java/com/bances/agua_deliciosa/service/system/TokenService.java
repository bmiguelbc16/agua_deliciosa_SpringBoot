package com.bances.agua_deliciosa.service.system;

import com.bances.agua_deliciosa.model.User;

public interface TokenService {
    String generateVerificationToken(User user);
    String generatePasswordResetToken(User user);
    boolean validateVerificationToken(String email, String token);
    boolean validateResetToken(String email, String token);
}