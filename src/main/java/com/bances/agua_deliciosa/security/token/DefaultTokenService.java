package com.bances.agua_deliciosa.security.token;

import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.model.UserToken;
import com.bances.agua_deliciosa.repository.UserTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DefaultTokenService implements TokenService {

    @Autowired
    private UserTokenRepository tokenRepository;

    @Override
    @Transactional
    public String generatePasswordResetToken(User user) {
        // Invalidar tokens anteriores
        deleteExpiredTokens();
        
        String token = generateToken();
        UserToken userToken = createUserToken(user, token);
        tokenRepository.save(userToken);
        return token;
    }

    @Override
    @Transactional
    public String generateVerificationToken(User user) {
        // Invalidar tokens anteriores
        deleteExpiredTokens();
        
        String token = generateToken();
        UserToken userToken = createUserToken(user, token);
        tokenRepository.save(userToken);
        return token;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateVerificationToken(String email, String token) {
        return tokenRepository.findByUserEmailAndTokenAndExpiresAtAfter(email, token, LocalDateTime.now())
                .isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateResetToken(String email, String token) {
        return tokenRepository.findByUserEmailAndTokenAndExpiresAtAfter(email, token, LocalDateTime.now())
                .isPresent();
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 0 * * * *") // Ejecutar cada hora
    public void deleteExpiredTokens() {
        tokenRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    private UserToken createUserToken(User user, String token) {
        UserToken userToken = new UserToken();
        userToken.setUser(user);
        userToken.setToken(token);
        // El @PrePersist en UserToken se encargará de establecer createdAt y expiresAt
        return userToken;
    }
}
