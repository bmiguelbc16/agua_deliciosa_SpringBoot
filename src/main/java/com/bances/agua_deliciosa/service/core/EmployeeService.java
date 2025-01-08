package com.bances.agua_deliciosa.service.core;

import com.bances.agua_deliciosa.exception.ResourceNotFoundException;
import com.bances.agua_deliciosa.model.Employee;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    @Transactional
    public Employee save(Employee employee, User user) {
        if (employeeRepository.existsByDocumentNumber(employee.getDocumentNumber())) {
            throw new RuntimeException("Document number already exists");
        }

        employee.setActive(true);
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());

        user.setUserableType("Employee");
        user.setUserableId(employee.getId());
        user.setRoleId(roleService.findEmployeeRole().getId());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        user = userService.save(user);
        employee.setUser(user);

        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee update(Long id, Employee updatedEmployee) {
        Employee employee = findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        if (!employee.getDocumentNumber().equals(updatedEmployee.getDocumentNumber()) &&
            employeeRepository.existsByDocumentNumberAndIdNot(updatedEmployee.getDocumentNumber(), id)) {
            throw new RuntimeException("Document number already exists");
        }

        employee.setUser(updatedEmployee.getUser());
        employee.setUpdatedAt(LocalDateTime.now());

        return employeeRepository.save(employee);
    }

    @Transactional
    public void deleteById(Long id) {
        Optional<Employee> employee = findById(id);
        if (employee.isPresent() && employee.get().getUser() != null) {
            userService.deleteById(employee.get().getUser().getId());
        }
        employeeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsByDocumentNumber(String documentNumber) {
        return employeeRepository.existsByDocumentNumber(documentNumber);
    }

    @Transactional(readOnly = true)
    public boolean existsByDocumentNumberAndIdNot(String documentNumber, Long id) {
        return employeeRepository.existsByDocumentNumberAndIdNot(documentNumber, id);
    }
}
