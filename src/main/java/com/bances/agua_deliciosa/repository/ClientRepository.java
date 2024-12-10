package com.bances.agua_deliciosa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.bances.agua_deliciosa.model.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("SELECT c FROM Client c JOIN c.user u WHERE u.name LIKE %:search% OR u.lastName LIKE %:search%")
    Page<Client> findByUserNameOrLastName(
        @Param("search") String search, 
        Pageable pageable
    );
    
    @Query("SELECT c FROM Client c JOIN c.user u WHERE u.email = :email")
    Client findByUserEmail(@Param("email") String email);
    
    @Query("SELECT c FROM Client c JOIN c.user u WHERE u.documentNumber = :documentNumber")
    Client findByUserDocumentNumber(@Param("documentNumber") String documentNumber);
    
    @Query("SELECT COUNT(c) FROM Client c WHERE c.user.active = true")
    Long countActiveClients();
} 