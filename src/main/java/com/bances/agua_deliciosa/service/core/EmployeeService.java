package com.bances.agua_deliciosa.service.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bances.agua_deliciosa.model.Employee;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.EmployeeRepository;
import com.bances.agua_deliciosa.repository.UserRepository;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Transactional
    public Employee save(Employee employee, User user) {
        validateNewEmployee(employee);
        
        // Encriptar contraseña y guardar usuario
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        
        // Asignar rol de empleado
        userService.assignRole(savedUser, "ROLE_EMPLOYEE");
        
        // Guardar empleado
        Employee savedEmployee = employeeRepository.save(employee);
        
        return savedEmployee;
    }

    @Transactional
    public Employee update(Employee employee) {
        Employee existingEmployee = findById(employee.getId());
        
        if (!existingEmployee.getEmail().equals(employee.getEmail())) {
            validateEmail(employee.getEmail());
        }
        
        if (!existingEmployee.getDocumentNumber().equals(employee.getDocumentNumber())) {
            validateDocumentNumber(employee.getDocumentNumber());
        }
        
        return employeeRepository.save(employee);
    }

    @Transactional
    public void delete(Long id) {
        Employee employee = findById(id);
        employeeRepository.delete(id);
        
        // Si tiene usuario asociado, desactivarlo también
        if (employee.getUser() != null) {
            userRepository.delete(employee.getUser().getId());
        }
    }

    private void validateNewEmployee(Employee employee) {
        validateEmail(employee.getEmail());
        validateDocumentNumber(employee.getDocumentNumber());
    }

    private void validateEmail(String email) {
        if (employeeRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists: " + email);
        }
    }

    private void validateDocumentNumber(String documentNumber) {
        if (employeeRepository.existsByDocumentNumber(documentNumber)) {
            throw new RuntimeException("Document number already exists: " + documentNumber);
        }
    }
}
