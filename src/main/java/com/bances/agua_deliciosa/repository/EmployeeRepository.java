package com.bances.agua_deliciosa.repository;

import com.bances.agua_deliciosa.model.Employee;
import com.bances.agua_deliciosa.model.Gender;
import com.bances.agua_deliciosa.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;

import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Employee> employeeMapper = (rs, rowNum) -> {
        Employee employee = new Employee();
        employee.setId(rs.getLong("id"));
        
        User user = new User();
        user.setName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setDocumentNumber(rs.getString("document_number"));
        user.setPhoneNumber(rs.getString("phone"));
        user.setBirthDate(rs.getDate("birth_date").toLocalDate());
        try {
            user.setGender(Gender.valueOf(rs.getString("gender")));
        } catch (IllegalArgumentException e) {
            user.setGender(Gender.UNKNOWN);
        }
        user.setActive(rs.getBoolean("active"));
        
        employee.setUser(user);
        employee.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        employee.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return employee;
    };

    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Employee> findAll() {
        String sql = "SELECT * FROM employees e JOIN users u ON e.id = u.userable_id WHERE u.userable_type = 'Employee'";
        return jdbcTemplate.query(sql, employeeMapper);
    }

    public Optional<Employee> findById(Long id) {
        String sql = "SELECT * FROM employees e JOIN users u ON e.id = u.userable_id WHERE e.id = ? AND u.userable_type = 'Employee'";
        List<Employee> employees = jdbcTemplate.query(sql, employeeMapper, id);
        return employees.isEmpty() ? Optional.empty() : Optional.of(employees.get(0));
    }

    public Optional<Employee> findByDocumentNumber(String documentNumber) {
        String sql = "SELECT * FROM employees e JOIN users u ON e.id = u.userable_id WHERE u.document_number = ? AND u.userable_type = 'Employee'";
        List<Employee> employees = jdbcTemplate.query(sql, employeeMapper, documentNumber);
        return employees.isEmpty() ? Optional.empty() : Optional.of(employees.get(0));
    }

    public Employee save(Employee employee) {
        if (employee.getId() == null) {
            // Primero insertamos el employee
            KeyHolder keyHolder = new GeneratedKeyHolder();
            String empSql = "INSERT INTO employees (created_at, updated_at) VALUES (?, ?)";
            
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(empSql, Statement.RETURN_GENERATED_KEYS);
                ps.setObject(1, employee.getCreatedAt());
                ps.setObject(2, employee.getUpdatedAt());
                return ps;
            }, keyHolder);

            Number key = keyHolder.getKey();
            if (key == null) {
                throw new RuntimeException("Failed to retrieve generated ID for employee");
            }
            employee.setId(key.longValue());

            // Luego insertamos el user
            String userSql = """
                INSERT INTO users (name, last_name, email, document_number, phone_number, 
                birth_date, gender, active, userable_type, userable_id, password, role_id) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'Employee', ?, ?, ?)
            """;
            jdbcTemplate.update(userSql,
                employee.getUser().getName(),
                employee.getUser().getLastName(),
                employee.getUser().getEmail(),
                employee.getUser().getDocumentNumber(),
                employee.getUser().getPhoneNumber(),
                employee.getUser().getBirthDate(),
                employee.getUser().getGender().toString(),
                employee.getUser().isActive(),
                employee.getId(),
                employee.getUser().getPassword(),
                employee.getUser().getRoleId()
            );
        } else {
            // Actualizamos employee
            String empSql = "UPDATE employees SET updated_at = ? WHERE id = ?";
            jdbcTemplate.update(empSql,
                employee.getUpdatedAt(),
                employee.getId()
            );

            // Actualizamos user
            String userSql = """
                UPDATE users SET 
                name = ?, last_name = ?, email = ?, document_number = ?, phone_number = ?,
                birth_date = ?, gender = ?, active = ?, password = ?, role_id = ?
                WHERE userable_id = ? AND userable_type = 'Employee'
            """;
            jdbcTemplate.update(userSql,
                employee.getUser().getName(),
                employee.getUser().getLastName(),
                employee.getUser().getEmail(),
                employee.getUser().getDocumentNumber(),
                employee.getUser().getPhoneNumber(),
                employee.getUser().getBirthDate(),
                employee.getUser().getGender().toString(),
                employee.getUser().isActive(),
                employee.getUser().getPassword(),
                employee.getUser().getRoleId(),
                employee.getId()
            );
        }
        return employee;
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM users WHERE userable_id = ? AND userable_type = 'Employee'", id);
        jdbcTemplate.update("DELETE FROM employees WHERE id = ?", id);
    }

    public boolean existsByDocumentNumber(String documentNumber) {
        String sql = "SELECT COUNT(*) FROM users WHERE document_number = ? AND userable_type = 'Employee'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, documentNumber);
        return count != null && count > 0;
    }

    public boolean existsByDocumentNumberAndIdNot(String documentNumber, Long id) {
        String sql = "SELECT COUNT(*) FROM users WHERE document_number = ? AND userable_type = 'Employee' AND userable_id != ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, documentNumber, id);
        return count != null && count > 0;
    }
}