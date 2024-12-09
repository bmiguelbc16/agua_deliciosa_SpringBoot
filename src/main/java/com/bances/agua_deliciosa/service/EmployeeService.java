package com.bances.agua_deliciosa.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import com.bances.agua_deliciosa.dto.EmployeeDTO;
import com.bances.agua_deliciosa.model.Employee;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.repository.EmployeeRepository;
import com.bances.agua_deliciosa.repository.UserRepository;
import com.bances.agua_deliciosa.repository.RoleRepository;

import java.util.Collections;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Employee findById(Long id) {
        return employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public Page<Employee> findAll(String search, Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            return employeeRepository.findByUserNameOrLastName(search, pageable);
        }
        return employeeRepository.findAll(pageable);
    }

    public Employee create(EmployeeDTO dto) {
        // Primero crear el employee
        Employee employee = new Employee();
        employeeRepository.save(employee);
        
        // Luego crear el usuario asociado
        User user = new User();
        user.setDocumentNumber(dto.getDni());
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setGender(dto.getGender());
        user.setPhoneNumber(dto.getTelefono());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUserableType("Employee");
        user.setUserableId(employee.getId());
        
        // Asignar rol
        Role role = roleRepository.findByName("ROLE_" + dto.getRole().toUpperCase())
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        user.setRoles(Collections.singleton(role));
        
        userRepository.save(user);
        
        return employee;
    }

    @Transactional
    public Employee update(Long id, EmployeeDTO dto) {
        Employee employee = findById(id);
        
        // Actualizar usuario
        User user = employee.getUser();
        user.setDocumentNumber(dto.getDni());
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setGender(dto.getGender());
        user.setPhoneNumber(dto.getTelefono());
        user.setEmail(dto.getEmail());
        
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        
        // Actualizar rol si cambió
        if (dto.getRole() != null) {
            Role newRole = roleRepository.findByName("ROLE_" + dto.getRole().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            user.getRoles().clear();
            user.getRoles().add(newRole);
        }
        
        userRepository.save(user);
        
        return employee;
    }

    @Transactional
    public void delete(Long id) {
        Employee employee = findById(id);
        employeeRepository.delete(employee);
        userRepository.delete(employee.getUser());
    }
} 