package com.bances.agua_deliciosa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.bances.agua_deliciosa.model.User;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    @Query("""
        SELECT DISTINCT u FROM User u 
        LEFT JOIN FETCH u.roles 
        WHERE u.id = :id
    """)
    Optional<User> findByIdWithRoles(@Param("id") Long id);
    
    @Query("""
        SELECT DISTINCT u FROM User u 
        LEFT JOIN FETCH u.roles 
        WHERE u.email = :email
    """)
    Optional<User> findByEmailWithRoles(@Param("email") String email);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByDocumentNumber(String documentNumber);
    
    Optional<User> findByRememberToken(String token);
    
    boolean existsByEmail(String email);
    
    boolean existsByDocumentNumber(String documentNumber);
}