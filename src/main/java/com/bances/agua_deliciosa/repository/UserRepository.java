package com.bances.agua_deliciosa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.bances.agua_deliciosa.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Operaciones CRUD estándar
    Optional<User> findByEmail(String email);
    Optional<User> findByDocumentNumber(String documentNumber);
    boolean existsByEmail(String email);
    boolean existsByDocumentNumber(String documentNumber);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByDocumentNumberAndIdNot(String documentNumber, Long id);

    // Consulta personalizada para obtener un usuario con su rol
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.role WHERE u.id = :id")
    Optional<User> findByIdWithRole(@Param("id") Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.role WHERE u.email = :email")
    Optional<User> findByEmailWithRole(@Param("email") String email);

    // Consulta para buscar por userableType y userableId
    @Query("SELECT u FROM User u WHERE u.userableType = :userableType AND u.userableId = :userableId")
    Optional<User> findByUserableTypeAndUserableId(
        @Param("userableType") String userableType, 
        @Param("userableId") Long userableId
    );
}
