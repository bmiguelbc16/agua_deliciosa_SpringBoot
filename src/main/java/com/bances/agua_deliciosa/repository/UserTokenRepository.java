package com.bances.agua_deliciosa.repository;

import com.bances.agua_deliciosa.model.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findByUserEmailAndTokenAndExpiresAtAfter(String email, String token, LocalDateTime now);
    void deleteByExpiresAtBefore(LocalDateTime dateTime);
}
