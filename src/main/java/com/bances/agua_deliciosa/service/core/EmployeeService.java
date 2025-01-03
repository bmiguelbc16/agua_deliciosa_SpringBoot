package com.bances.agua_deliciosa.service.core;

import java.util.List;
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
import com.bances.agua_deliciosa.repository.EmployeeRepository;
import com.bances.agua_deliciosa.model.Role;
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

    @Transactional
    public Employee createEmployee(EmployeeDTO dto) {
        Employee employee = new Employee();
        employeeRepository.save(employee);

        User user = new User();
        updateUserFromDTO(user, dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUserableType("Employee");
        user.setUserableId(employee.getId());

        Role role = roleRepository.findByName(dto.getRoles())
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        user.setRole(role);

        userRepository.save(user);
        return employee;
    }

    private void updateUserFromDTO(User user, EmployeeDTO dto) {
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setDocumentNumber(dto.getDocumentNumber());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setBirthDate(dto.getBirthDate());
        user.setGender(dto.getGender());
        user.setActive(dto.getActive());
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
    }

    @Transactional
    public Employee update(Long id, EmployeeDTO dto) {
        Employee employee = getEmployeeById(id);
        User user = employee.getUser();
        
        try {
            // Solo validar si el email ha cambiado
            if (!dto.getEmail().equals(user.getEmail())) {
                if (userRepository.existsByEmailAndIdNot(dto.getEmail(), user.getId())) {
                    throw new RuntimeException("El email ya está registrado por otro usuario");
                }
            }
            
            // Solo validar si el DNI ha cambiado
            if (!dto.getDocumentNumber().equals(user.getDocumentNumber())) {
                if (userRepository.existsByDocumentNumberAndIdNot(dto.getDocumentNumber(), user.getId())) {
                    throw new RuntimeException("El DNI ya está registrado por otro usuario");
                }
            }
            
            // Actualizar datos del usuario
            updateUserFromDTO(user, dto);
            
            // Solo actualizar la contraseña si se proporciona una nueva
            String newPassword = dto.getPassword();
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
            
            user.setUpdatedAt(LocalDateTime.now());
            
            // Actualizar rol
            Role role = roleRepository.findByName(dto.getRoles())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            user.setRole(role);
            
            // Guardar cambios
            userRepository.save(user);
            return employeeRepository.save(employee);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el empleado: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = getEmployeeById(id);
        User user = employee.getUser();
        if (user != null) {
            userRepository.delete(user);
        }
        employeeRepository.deleteById(id);
    }

    public Page<Employee> getEmployeesPage(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }
}
