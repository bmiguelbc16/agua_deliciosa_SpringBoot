package com.bances.agua_deliciosa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.bances.agua_deliciosa.model.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT DISTINCT e FROM Employee e JOIN FETCH e.user u LEFT JOIN FETCH u.roles")
    Page<Employee> findAllWithUserAndRoles(Pageable pageable);

    @Query("SELECT DISTINCT e FROM Employee e JOIN FETCH e.user u LEFT JOIN FETCH u.roles " +
           "WHERE u.name LIKE %:search% OR u.lastName LIKE %:search%")
    Page<Employee> findByUserNameOrLastName(
        @Param("search") String search, 
        Pageable pageable
    );
    
    @Query("SELECT e FROM Employee e JOIN e.user u JOIN u.roles r WHERE r.name = :roleName")
    Page<Employee> findByRole(@Param("roleName") String roleName, Pageable pageable);
    
    @Query("SELECT e FROM Employee e JOIN e.user u WHERE u.email = :email")
    Employee findByUserEmail(@Param("email") String email);
    
    @Query("SELECT e FROM Employee e JOIN e.user u WHERE u.documentNumber = :documentNumber")
    Employee findByUserDocumentNumber(@Param("documentNumber") String documentNumber);
    
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.user.active = true")
    Long countActiveEmployees();
} 