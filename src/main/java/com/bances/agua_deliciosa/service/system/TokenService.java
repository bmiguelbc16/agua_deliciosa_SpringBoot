package com.bances.agua_deliciosa.service.system;

import com.bances.agua_deliciosa.model.User;

public interface TokenService {
    String generatePasswordResetToken(User user);
    boolean validateVerificationToken(User user, String token);
    boolean validateResetToken(User user, String token);
} 