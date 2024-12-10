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
        return employeeRepository.findAllWithUserAndRoles(pageable);
    }

    @Transactional
    public Employee create(EmployeeDTO dto) {
        // Validar que el email no exista
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        
        // Validar que el DNI no exista
        if (userRepository.existsByDocumentNumber(dto.getDocumentNumber())) {
            throw new RuntimeException("El número de documento ya está registrado");
        }

        // Crear el empleado primero
        Employee employee = new Employee();
        employeeRepository.save(employee);
        
        // Crear el usuario asociado
        User user = new User();
        user.setDocumentNumber(dto.getDocumentNumber());
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setGender(dto.getGender());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUserableType("Employee");
        user.setUserableId(employee.getId());
        user.setBirthDate(dto.getBirthDate());
        user.setActive(dto.isActive());
        
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
        User user = employee.getUser();
        
        // Validar contraseñas si se está actualizando
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                throw new RuntimeException("Las contraseñas no coinciden");
            }
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        
        // Resto del código de actualización...
        user.setDocumentNumber(dto.getDocumentNumber());
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setGender(dto.getGender());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setEmail(dto.getEmail());
        user.setBirthDate(dto.getBirthDate());
        user.setActive(dto.isActive());
        
        // Actualizar rol
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