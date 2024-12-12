package com.bances.agua_deliciosa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bances.agua_deliciosa.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
    @Query("SELECT c FROM Client c WHERE c.user.name LIKE %:search% OR c.user.lastName LIKE %:search%")
    Page<Client> findByUserNameContainingOrLastNameContaining(
        @Param("search") String search, 
        Pageable pageable
    );
    
    @Query("SELECT c FROM Client c WHERE c.user.email = :email")
    Optional<Client> findByUserEmail(@Param("email") String email);
    
    @Query("SELECT c FROM Client c WHERE c.user.documentNumber = :documentNumber")
    Optional<Client> findByUserDocumentNumber(@Param("documentNumber") String documentNumber);
    
    @Query("SELECT COUNT(c) FROM Client c WHERE c.user.active = true")
    Long countActiveClients();
} 