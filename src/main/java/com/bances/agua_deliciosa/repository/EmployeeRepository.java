package com.bances.agua_deliciosa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.bances.agua_deliciosa.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("""
        SELECT DISTINCT e FROM Employee e 
        JOIN FETCH e.user u 
        WHERE u.userableType = 'Employee'
        AND (:search IS NULL 
            OR u.name LIKE %:search% 
            OR u.lastName LIKE %:search% 
            OR u.documentNumber LIKE %:search%)
        """)
    Page<Employee> findEmployeesWithSearch(@Param("search") String search, Pageable pageable);
}
