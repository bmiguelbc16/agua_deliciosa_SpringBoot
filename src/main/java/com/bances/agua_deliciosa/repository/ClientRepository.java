package com.bances.agua_deliciosa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.bances.agua_deliciosa.model.Client;
import org.springframework.lang.NonNull;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("""
        SELECT DISTINCT c FROM Client c 
        JOIN FETCH c.user u 
        WHERE u.documentNumber LIKE %:search% 
           OR u.name LIKE %:search% 
           OR u.lastName LIKE %:search%
    """)
    Page<Client> findBySearchTerm(@Param("search") String search, Pageable pageable);

    @Override
    @NonNull
    @Query("""
        SELECT DISTINCT c FROM Client c 
        JOIN FETCH c.user u
    """)
    Page<Client> findAll(@NonNull Pageable pageable);

    @Query(value = """
        SELECT COUNT(c.*) > 0 FROM clients c 
        INNER JOIN users u ON c.id = u.userable_id 
        WHERE u.userable_type = 'Client'
        AND u.document_number = :documentNumber
    """, nativeQuery = true)
    boolean existsByDocumentNumber(@Param("documentNumber") String documentNumber);

    @Query(value = """
        SELECT COUNT(c.*) > 0 FROM clients c 
        INNER JOIN users u ON c.id = u.userable_id 
        WHERE u.userable_type = 'Client'
        AND u.email = :email
    """, nativeQuery = true)
    boolean existsByEmail(@Param("email") String email);
}
