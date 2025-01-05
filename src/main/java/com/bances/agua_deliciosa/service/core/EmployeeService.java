package com.bances.agua_deliciosa.service.core;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bances.agua_deliciosa.dto.admin.EmployeeDTO;
import com.bances.agua_deliciosa.model.Employee;
import com.bances.agua_deliciosa.model.Gender;
import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.EmployeeRepository;
import com.bances.agua_deliciosa.repository.RoleRepository;
import com.bances.agua_deliciosa.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<Employee> getEmployeesPage(Pageable pageable) {
        return employeeRepository.findByUserUserableType("Employee", pageable);
    }

    @Transactional
    public Employee createEmployee(EmployeeDTO employeeDTO) {
        // Validar si el email ya existe
        if (userRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Validar si el documento ya existe
        if (userRepository.existsByDocumentNumber(employeeDTO.getDocumentNumber())) {
            throw new RuntimeException("El DNI ya está registrado");
        }

        // Crear y guardar el empleado
        Employee employee = new Employee();
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());
        employee = employeeRepository.save(employee);

        // Crear y guardar el usuario asociado
        User user = new User();
        user.setName(employeeDTO.getName());
        user.setLastName(employeeDTO.getLastName());
        user.setEmail(employeeDTO.getEmail());
        user.setDocumentNumber(employeeDTO.getDocumentNumber());
        user.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        user.setBirthDate(employeeDTO.getBirthDate());
        user.setGender(Gender.valueOf(employeeDTO.getGender().toUpperCase()));
        user.setPhoneNumber(employeeDTO.getPhoneNumber());
        user.setRole(roleRepository.findById(employeeDTO.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found")));
        user.setUserableId(employee.getId());
        user.setUserableType("Employee");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        return employee;
    }

    private void updateUserFromDTO(User user, EmployeeDTO dto) {
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setDocumentNumber(dto.getDocumentNumber());
        user.setBirthDate(dto.getBirthDate());
        user.setGender(Gender.valueOf(dto.getGender().toUpperCase()));
    }

    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        
        User user = userRepository.findByUserableIdAndUserableType(employee.getId(), "Employee")
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado para el empleado"));
        
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(user.getName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setDocumentNumber(user.getDocumentNumber());
        dto.setBirthDate(user.getBirthDate());
        dto.setActive(user.isActive());
        dto.setRoleId(user.getRole().getId());
        dto.setGender(user.getGender().toString());
        
        return dto;
    }

    @Transactional
    public Employee update(Long id, EmployeeDTO dto) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
            
        User user = userRepository.findByUserableIdAndUserableType(employee.getId(), "Employee")
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado para el empleado"));
        
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
        user.setActive(dto.getActive());
        
        // Actualizar rol
        Role role = roleRepository.findById(dto.getRoleId())
            .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + dto.getRoleId()));
        user.setRole(role);
        
        // Actualizar contraseña si se proporciona una nueva
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        
        userRepository.save(user);
        
        // Recargar el empleado con el usuario actualizado
        return employeeRepository.findById(employee.getId())
            .orElseThrow(() -> new RuntimeException("Error al actualizar el empleado"));
    }

    @Transactional
    public Employee updateEmployee(Long id, EmployeeDTO dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        User user = employee.getUser();
        if (user == null) {
            throw new RuntimeException("User not found for employee");
        }

        // Validar email único
        if (!user.getEmail().equals(dto.getEmail()) && 
            userRepository.existsByEmailAndIdNot(dto.getEmail(), user.getId())) {
            throw new RuntimeException("Email already exists");
        }

        // Validar documento único
        if (!user.getDocumentNumber().equals(dto.getDocumentNumber()) && 
            userRepository.existsByDocumentNumberAndIdNot(dto.getDocumentNumber(), user.getId())) {
            throw new RuntimeException("Document number already exists");
        }

        updateUserFromDTO(user, dto);
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getRoleId() != null) {
            Role role = roleRepository.findById(dto.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            user.setRole(role);
        }

        userRepository.save(user);
        return employee;
    }

    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        User user = userRepository.findByUserableIdAndUserableType(employee.getId(), "Employee")
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado para el empleado"));
        
        if (user != null) {
            userRepository.delete(user);
        }
        
        employeeRepository.delete(employee);
    }
}
