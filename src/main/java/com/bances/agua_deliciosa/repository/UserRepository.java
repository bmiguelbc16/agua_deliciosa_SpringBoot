package com.bances.agua_deliciosa.repository;

import com.bances.agua_deliciosa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByDocumentNumber(String documentNumber);
    Optional<User> findByRememberToken(String token);
}