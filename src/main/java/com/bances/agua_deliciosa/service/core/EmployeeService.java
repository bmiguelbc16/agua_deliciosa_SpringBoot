package com.bances.agua_deliciosa.service.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bances.agua_deliciosa.model.Employee;
import com.bances.agua_deliciosa.model.Gender;
import com.bances.agua_deliciosa.dto.admin.EmployeeDTO;
import com.bances.agua_deliciosa.repository.EmployeeRepository;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.UserRepository;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<Employee> getEmployeesPage(Pageable pageable) {
        return employeeRepository.findEmployeesWithSearch(null, pageable);
    }

    @Transactional
    public Employee createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        User user = new User();
        
        // Mapear datos del DTO al usuario
        user.setName(employeeDTO.getName());
        user.setLastName(employeeDTO.getLastName());
        user.setEmail(employeeDTO.getEmail());
        user.setPhoneNumber(employeeDTO.getPhoneNumber());
        user.setDocumentNumber(employeeDTO.getDocumentNumber());
        user.setBirthDate(employeeDTO.getBirthDate());
        user.setGender(Gender.valueOf(employeeDTO.getGender()));
        user.setActive(employeeDTO.getActive());
        user.setUserableType("Employee");
        
        // Guardar el usuario
        user = userRepository.save(user);
        
        // Asociar el usuario al empleado
        employee.setUser(user);
        
        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        User user = employee.getUser();
        
        // Actualizar datos del usuario
        user.setName(employeeDTO.getName());
        user.setLastName(employeeDTO.getLastName());
        user.setEmail(employeeDTO.getEmail());
        user.setPhoneNumber(employeeDTO.getPhoneNumber());
        user.setDocumentNumber(employeeDTO.getDocumentNumber());
        user.setBirthDate(employeeDTO.getBirthDate());
        user.setGender(Gender.valueOf(employeeDTO.getGender()));
        user.setActive(employeeDTO.getActive());
        
        // Guardar los cambios
        userRepository.save(user);
        return employeeRepository.save(employee);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        // Eliminar el usuario asociado
        if (employee.getUser() != null) {
            userRepository.delete(employee.getUser());
        }
        
        employeeRepository.delete(employee);
    }
}
