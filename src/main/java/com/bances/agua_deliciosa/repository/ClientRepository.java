package com.bances.agua_deliciosa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.bances.agua_deliciosa.model.Client;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("""
        SELECT DISTINCT c FROM Client c 
        JOIN FETCH c.user u 
        WHERE u.userableType = 'Client'
        AND (:search IS NULL 
            OR u.name LIKE %:search% 
            OR u.lastName LIKE %:search% 
            OR u.documentNumber LIKE %:search%)
        """)
    Page<Client> findClientsWithSearch(@Param("search") String search, Pageable pageable);

    @Query("SELECT c FROM Client c JOIN FETCH c.user u WHERE u.id = :userId AND u.userableType = 'Client'")
    Optional<Client> findByUserId(@Param("userId") Long userId);
}
