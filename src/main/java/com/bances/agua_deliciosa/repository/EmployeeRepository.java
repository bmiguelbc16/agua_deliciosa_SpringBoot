package com.bances.agua_deliciosa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.bances.agua_deliciosa.model.Employee;
import org.springframework.lang.NonNull;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query(value = """
        SELECT e.* FROM employees e 
        INNER JOIN (
            SELECT DISTINCT userable_id 
            FROM users 
            WHERE userable_type = 'Employee'
            AND (document_number LIKE CONCAT('%', :search, '%') 
                OR name LIKE CONCAT('%', :search, '%') 
                OR last_name LIKE CONCAT('%', :search, '%'))
        ) u ON e.id = u.userable_id
    """, 
    countQuery = """
        SELECT COUNT(DISTINCT e.id) 
        FROM employees e 
        INNER JOIN users u ON e.id = u.userable_id 
        WHERE u.userable_type = 'Employee'
        AND (u.document_number LIKE CONCAT('%', :search, '%') 
            OR u.name LIKE CONCAT('%', :search, '%') 
            OR u.last_name LIKE CONCAT('%', :search, '%'))
    """,
    nativeQuery = true)
    Page<Employee> findBySearchTerm(@Param("search") String search, Pageable pageable);

    @Override
    @NonNull
    @Query(value = """
        SELECT e.* FROM employees e 
        INNER JOIN (
            SELECT DISTINCT userable_id 
            FROM users 
            WHERE userable_type = 'Employee'
        ) u ON e.id = u.userable_id
    """, 
    countQuery = """
        SELECT COUNT(DISTINCT e.id) 
        FROM employees e 
        INNER JOIN users u ON e.id = u.userable_id 
        WHERE u.userable_type = 'Employee'
    """, 
    nativeQuery = true)
    Page<Employee> findAll(@NonNull Pageable pageable);

    @Query(value = """
        SELECT COUNT(*) > 0 FROM employees e 
        INNER JOIN users u ON e.id = u.userable_id 
        WHERE u.userable_type = 'Employee'
        AND u.document_number = :documentNumber
    """, nativeQuery = true)
    boolean existsByDocumentNumber(@Param("documentNumber") String documentNumber);

    @Query(value = """
        SELECT COUNT(*) > 0 FROM employees e 
        INNER JOIN users u ON e.id = u.userable_id 
        WHERE u.userable_type = 'Employee'
        AND u.email = :email
    """, nativeQuery = true)
    boolean existsByEmail(@Param("email") String email);
}
