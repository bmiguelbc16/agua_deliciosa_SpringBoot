package com.bances.agua_deliciosa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.bances.agua_deliciosa.model.Client;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
    @Query(value = """
            SELECT DISTINCT c FROM Client c 
            JOIN FETCH c.user u 
            LEFT JOIN FETCH u.role r 
            WHERE u.userableType = 'Client'
            AND (:search IS NULL 
                OR u.documentNumber LIKE CONCAT('%', :search, '%')
                OR u.name LIKE CONCAT('%', :search, '%')
                OR u.lastName LIKE CONCAT('%', :search, '%'))
            ORDER BY c.id DESC
            """, 
          countQuery = """
            SELECT COUNT(DISTINCT c) FROM Client c 
            JOIN c.user u 
            WHERE u.userableType = 'Client'
            AND (:search IS NULL 
                OR u.documentNumber LIKE CONCAT('%', :search, '%')
                OR u.name LIKE CONCAT('%', :search, '%')
                OR u.lastName LIKE CONCAT('%', :search, '%'))
            """)
    Page<Client> findByUserUserableTypeAndSearchTerm(@Param("search") String search, Pageable pageable);

    @Query(value = """
            SELECT DISTINCT c FROM Client c 
            JOIN FETCH c.user u 
            LEFT JOIN FETCH u.role r 
            WHERE u.userableType = 'Client'
            ORDER BY c.id DESC
            """,
          countQuery = """
            SELECT COUNT(DISTINCT c) FROM Client c 
            JOIN c.user u 
            WHERE u.userableType = 'Client'
            """)
    Page<Client> findByUserUserableType(Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Client c " +
           "JOIN c.user u WHERE u.documentNumber = :documentNumber")
    boolean existsByDocumentNumber(@Param("documentNumber") String documentNumber);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Client c " +
           "JOIN c.user u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);

    Optional<Client> findByUserId(Long userId);
}
