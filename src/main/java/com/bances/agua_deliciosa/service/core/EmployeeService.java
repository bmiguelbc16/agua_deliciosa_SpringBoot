package com.bances.agua_deliciosa.service.core;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;

import com.bances.agua_deliciosa.dto.admin.EmployeeDTO;
import com.bances.agua_deliciosa.model.Employee;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.model.Gender;
import com.bances.agua_deliciosa.repository.EmployeeRepository;
import com.bances.agua_deliciosa.repository.UserRepository;
import com.bances.agua_deliciosa.repository.RoleRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<Employee> getEmployeesPage(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    @Transactional
    public Employee createEmployee(EmployeeDTO dto) {
        // Validar email único
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Validar DNI único
        if (userRepository.existsByDocumentNumber(dto.getDocumentNumber())) {
            throw new RuntimeException("El DNI ya está registrado");
        }

        Employee employee = new Employee();
        employeeRepository.save(employee);

        User user = new User();
        updateUserFromDTO(user, dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUserableType("Employee");
        user.setUserableId(employee.getId());
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());

        // Buscar y asignar el rol
        Role role = roleRepository.findByName(dto.getRoles())
            .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + dto.getRoles()));
        user.setRole(role);

        userRepository.save(user);
        return employee;
    }

    private void updateUserFromDTO(User user, EmployeeDTO dto) {
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setDocumentNumber(dto.getDocumentNumber());
        user.setGender(Gender.valueOf(dto.getGender()));
        user.setBirthDate(dto.getBirthDate());
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
    }

    @Transactional
    public Employee update(Long id, EmployeeDTO dto) {
        Employee employee = getEmployeeById(id);
        User user = employee.getUser();
        
        // Validar email único
        if (!dto.getEmail().equals(user.getEmail()) && 
            userRepository.existsByEmailAndIdNot(dto.getEmail(), user.getId())) {
            throw new RuntimeException("El email ya está registrado por otro usuario");
        }
        
        // Validar DNI único
        if (!dto.getDocumentNumber().equals(user.getDocumentNumber()) && 
            userRepository.existsByDocumentNumberAndIdNot(dto.getDocumentNumber(), user.getId())) {
            throw new RuntimeException("El DNI ya está registrado por otro usuario");
        }
        
        // Actualizar datos del usuario
        updateUserFromDTO(user, dto);
        
        // Actualizar contraseña si se proporciona una nueva
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        
        // Actualizar rol si ha cambiado
        if (!user.getRole().getName().equals(dto.getRoles())) {
            Role role = roleRepository.findByName(dto.getRoles())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + dto.getRoles()));
            user.setRole(role);
        }
        
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        
        return employee;
    }

    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = getEmployeeById(id);
        User user = employee.getUser();
        
        if (user != null) {
            userRepository.delete(user);
        }
        
        employeeRepository.delete(employee);
    }
}
