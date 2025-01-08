package com.bances.agua_deliciosa.security.token;

import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.model.UserToken;
import com.bances.agua_deliciosa.repository.UserTokenRepository;
import com.bances.agua_deliciosa.service.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultTokenService implements TokenService {
    private static final int TOKEN_EXPIRATION_DAYS = 7;

    private final UserTokenRepository userTokenRepository;
    private final UserService userService;

    @Override
    @Transactional
    public String generateVerificationToken(User user) {
        return generateTokenForUser(user);
    }

    @Override
    @Transactional
    public String generatePasswordResetToken(User user) {
        return generateTokenForUser(user);
    }

    private String generateTokenForUser(User user) {
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        UserToken userToken = new UserToken();
        userToken.setUserId(user.getId());
        userToken.setToken(token);
        userToken.setCreatedAt(now);
        userToken.setExpiresAt(now.plusDays(TOKEN_EXPIRATION_DAYS));
        userToken.setUpdatedAt(now);

        userTokenRepository.save(userToken);

        return token;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateVerificationToken(String email, String token) {
        Optional<User> user = userService.findByEmail(email);
        if (user.isEmpty()) {
            return false;
        }

        Optional<UserToken> userToken = userTokenRepository.findByToken(token);
        if (userToken.isEmpty()) {
            return false;
        }

        return userToken.get().getUserId().equals(user.get().getId()) &&
               userToken.get().getExpiresAt().isAfter(LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateResetToken(String email, String token) {
        return validateVerificationToken(email, token);
    }

    @Override
    @Transactional
    public void deleteExpiredTokens() {
        userTokenRepository.deleteExpiredTokens();
    }
}
