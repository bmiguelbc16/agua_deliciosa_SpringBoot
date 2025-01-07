package com.bances.agua_deliciosa.security.token;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.model.UserToken;
import com.bances.agua_deliciosa.repository.UserTokenRepository;
import com.bances.agua_deliciosa.service.system.TokenService;

@Service
public class DefaultTokenService implements TokenService {

    private static final int TOKEN_EXPIRATION_DAYS = 7;

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Override
    @Transactional
    public String generateVerificationToken(User user) {
        LocalDateTime now = LocalDateTime.now();
        UserToken userToken = createToken(user, now);
        userToken.setUserId(user.getId());
        return userToken.getToken();
    }

    @Override
    @Transactional
    public String generatePasswordResetToken(User user) {
        LocalDateTime now = LocalDateTime.now();
        UserToken userToken = createToken(user, now);
        userToken.setUserId(user.getId());
        return userToken.getToken();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateVerificationToken(String email, String token) {
        return userTokenRepository.findByUserEmailAndTokenAndExpiresAtAfter(email, token, LocalDateTime.now())
                .isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateResetToken(String email, String token) {
        return userTokenRepository.findByUserEmailAndTokenAndExpiresAtAfter(email, token, LocalDateTime.now())
                .isPresent();
    }

    @Transactional
    @Scheduled(cron = "0 0 * * * *") // Ejecutar cada hora
    public void deleteExpiredTokens() {
        userTokenRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    private UserToken createToken(User user, LocalDateTime now) {
        UserToken token = new UserToken();
        token.setUserId(user.getId());
        token.setToken(generateToken());
        token.setExpiresAt(now.plusDays(TOKEN_EXPIRATION_DAYS));
        token.setActive(true);
        token.setCreatedAt(now);
        token.setUpdatedAt(now);
        return userTokenRepository.save(token);
    }
}
