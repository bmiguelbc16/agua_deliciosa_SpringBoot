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
    @Query("SELECT e FROM Employee e JOIN e.user u WHERE u.name LIKE %:search% OR u.lastName LIKE %:search%")
    Page<Employee> findByUserNameOrLastName(
        @Param("search") String search, 
        Pageable pageable
    );
} 