package com.bances.agua_deliciosa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.model.UserVerification;
import com.bances.agua_deliciosa.model.VerificationType;

@Repository
public interface UserVerificationRepository extends JpaRepository<UserVerification, Long> {
    
    Optional<UserVerification> findByTokenAndVerificationType(String token, VerificationType type);
    
    Optional<UserVerification> findByUserAndVerificationType(User user, VerificationType type);
}
