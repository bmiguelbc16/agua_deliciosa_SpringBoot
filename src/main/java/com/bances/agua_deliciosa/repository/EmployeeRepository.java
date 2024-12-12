package com.bances.agua_deliciosa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.bances.agua_deliciosa.model.Employee;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    @Query("SELECT e FROM Employee e WHERE e.user.name LIKE %:search% OR e.user.lastName LIKE %:search%")
    Page<Employee> findByUserNameContainingOrLastNameContaining(
        @Param("search") String search, 
        Pageable pageable
    );
    
    @Query("SELECT e FROM Employee e WHERE e.user.email = :email")
    Optional<Employee> findByUserEmail(@Param("email") String email);
    
    @Query("SELECT e FROM Employee e WHERE e.user.documentNumber = :documentNumber")
    Optional<Employee> findByUserDocumentNumber(@Param("documentNumber") String documentNumber);
    
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.user.active = true")
    Long countActiveEmployees();
} 