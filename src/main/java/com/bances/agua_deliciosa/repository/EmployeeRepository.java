package com.bances.agua_deliciosa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;

import com.bances.agua_deliciosa.model.Employee;

/**
 * Repository para la entidad Employee.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    /**
     * Consulta personalizada para cargar empleados con sus usuarios y roles con paginación.
     *
     * @param pageable Objeto de paginación.
     * @return Página de empleados con sus usuarios y roles.
     */
    @Query(value = """
            SELECT e FROM Employee e 
            LEFT JOIN FETCH e.user u 
            LEFT JOIN FETCH u.role r 
            WHERE u.userableType = 'Employee'
            ORDER BY e.id DESC
            """,
            countQuery = """
            SELECT COUNT(e) FROM Employee e 
            LEFT JOIN e.user u
            WHERE u.userableType = 'Employee'
            """)
    Page<Employee> findAllWithUsers(Pageable pageable);

    /**
     * Consulta personalizada para buscar empleados por un término de búsqueda.
     *
     * @param search Término de búsqueda.
     * @param pageable Objeto de paginación.
     * @return Página de empleados que coinciden con el término de búsqueda.
     */
    @Query(value = """
        SELECT e FROM Employee e 
        LEFT JOIN FETCH e.user u 
        LEFT JOIN FETCH u.role r 
        WHERE u.userableType = 'Employee'
        AND (
            LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(u.documentNumber) LIKE LOWER(CONCAT('%', :search, '%'))
        )
        ORDER BY e.id DESC
        """,
        countQuery = """
        SELECT COUNT(e) FROM Employee e 
        LEFT JOIN e.user u
        WHERE u.userableType = 'Employee'
        AND (
            LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(u.documentNumber) LIKE LOWER(CONCAT('%', :search, '%'))
        )
        """)
    Page<Employee> findBySearchTerm(@Param("search") String search, Pageable pageable);

    /**
     * Método para obtener solo empleados (excluyendo clientes) con paginación.
     *
     * @param pageable Objeto de paginación.
     * @return Página de empleados.
     */
    @Query(value = """
            SELECT e.* FROM employees e 
            INNER JOIN users u ON e.id = u.userable_id AND u.userable_type = 'Employee'
            ORDER BY e.id DESC
            """,
          countQuery = """
            SELECT COUNT(*) FROM employees e 
            INNER JOIN users u ON e.id = u.userable_id AND u.userable_type = 'Employee'
            """,
          nativeQuery = true)
    Page<Employee> findAllEmployees(Pageable pageable);

    /**
     * Método para verificar si existe un empleado por su documento.
     *
     * @param documentNumber Número de documento.
     * @return true si existe, false en caso contrario.
     */
    @Query("""
        SELECT COUNT(e) > 0 FROM Employee e 
        LEFT JOIN e.user u 
        WHERE u.documentNumber = :documentNumber
        """)
    boolean existsByDocumentNumber(@Param("documentNumber") String documentNumber);

    /**
     * Método para verificar si existe un empleado por su email.
     *
     * @param email Email del empleado.
     * @return true si existe, false en caso contrario.
     */
    @Query("""
        SELECT COUNT(e) > 0 FROM Employee e 
        LEFT JOIN e.user u 
        WHERE u.email = :email
        """)
    boolean existsByEmail(@Param("email") String email);

    /**
     * Método para verificar si existe un empleado por su ID.
     *
     * @param id ID del empleado.
     * @return true si existe, false en caso contrario.
     */
    @Query("SELECT COUNT(e) > 0 FROM Employee e WHERE e.id = :id")
    boolean existsById(@Param("id") @NonNull Long id);

    Page<Employee> findByUserUserableType(String userableType, Pageable pageable);
}
