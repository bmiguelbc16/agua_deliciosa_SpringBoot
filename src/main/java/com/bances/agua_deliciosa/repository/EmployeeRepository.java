package com.bances.agua_deliciosa.repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bances.agua_deliciosa.model.Employee;

@Repository
public class EmployeeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Employee> employeeMapper = (ResultSet rs, int rowNum) -> {
        Employee employee = new Employee();
        employee.setId(rs.getLong("id"));
        employee.setName(rs.getString("name"));
        employee.setLastName(rs.getString("last_name"));
        employee.setEmail(rs.getString("email"));
        employee.setPhoneNumber(rs.getString("phone_number"));
        employee.setAddress(rs.getString("address"));
        employee.setDocumentNumber(rs.getString("document_number"));
        employee.setPosition(rs.getString("position"));
        employee.setSalary(rs.getBigDecimal("salary"));
        employee.setActive(rs.getBoolean("active"));
        return employee;
    };

    public Optional<Employee> findById(Long id) {
        String sql = "SELECT * FROM employees WHERE id = ? AND active = true";
        List<Employee> employees = jdbcTemplate.query(sql, employeeMapper, id);
        return employees.isEmpty() ? Optional.empty() : Optional.of(employees.get(0));
    }

    public List<Employee> findAll() {
        String sql = "SELECT * FROM employees WHERE active = true";
        return jdbcTemplate.query(sql, employeeMapper);
    }

    public Employee save(Employee employee) {
        if (employee.getId() == null) {
            return insert(employee);
        }
        return update(employee);
    }

    private Employee insert(Employee employee) {
        String sql = "INSERT INTO employees (name, last_name, email, phone_number, address, " +
                    "document_number, position, salary, active) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        jdbcTemplate.update(sql,
            employee.getName(),
            employee.getLastName(),
            employee.getEmail(),
            employee.getPhoneNumber(),
            employee.getAddress(),
            employee.getDocumentNumber(),
            employee.getPosition(),
            employee.getSalary(),
            true
        );
        
        Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        if (id != null) {
            employee.setId(id);
        }
        
        return employee;
    }

    private Employee update(Employee employee) {
        String sql = "UPDATE employees SET name = ?, last_name = ?, email = ?, " +
                    "phone_number = ?, address = ?, document_number = ?, " +
                    "position = ?, salary = ?, active = ? " +
                    "WHERE id = ?";
        
        jdbcTemplate.update(sql,
            employee.getName(),
            employee.getLastName(),
            employee.getEmail(),
            employee.getPhoneNumber(),
            employee.getAddress(),
            employee.getDocumentNumber(),
            employee.getPosition(),
            employee.getSalary(),
            employee.isActive(),
            employee.getId()
        );
        
        return employee;
    }

    public void delete(Long id) {
        String sql = "UPDATE employees SET active = false WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM employees WHERE email = ? AND active = true";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public boolean existsByDocumentNumber(String documentNumber) {
        String sql = "SELECT COUNT(*) FROM employees WHERE document_number = ? AND active = true";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, documentNumber);
        return count != null && count > 0;
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM employees WHERE active = true";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0L;
    }
}