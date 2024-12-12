package com.bances.agua_deliciosa.service.core;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.bances.agua_deliciosa.dto.admin.EmployeeDTO;
import com.bances.agua_deliciosa.model.Employee;
import com.bances.agua_deliciosa.repository.EmployeeRepository;
import com.bances.agua_deliciosa.service.auth.CoreUserService;
import com.bances.agua_deliciosa.service.core.base.BaseService;
import com.bances.agua_deliciosa.model.User;
import org.modelmapper.ModelMapper;

@Service
@Transactional(readOnly = true)
public class EmployeeService extends BaseService<Employee, EmployeeDTO> {
    
    private final CoreUserService userService;
    private final EmployeeRepository employeeRepository;
    
    public EmployeeService(
        EmployeeRepository repository, 
        CoreUserService userService,
        ModelMapper mapper
    ) {
        super(repository, mapper);
        this.employeeRepository = repository;
        this.userService = userService;
    }
    
    @Override
    protected String getEntityName() {
        return "Empleado";
    }
    
    public Page<Employee> findAll(String search, Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            return employeeRepository.findByUserNameContainingOrLastNameContaining(search, pageable);
        }
        return repository.findAll(pageable);
    }
    
    @Override
    protected Employee createFromDTO(EmployeeDTO dto) {
        validateEmployeeDTO(dto);
        Employee employee = new Employee();
        User user = createUserFromDTO(dto);
        employee.setUser(user);
        return employee;
    }
    
    private void validateEmployeeDTO(EmployeeDTO dto) {
        if (dto.getRole() == null || dto.getRole().isEmpty()) {
            throw new RuntimeException("El rol es requerido");
        }
    }
    
    private User createUserFromDTO(EmployeeDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setDocumentNumber(dto.getDocumentNumber());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setUserableType("Employee");
        
        return userService.createUser(user, dto.getPassword(), dto.getRole());
    }
    
    @Override
    protected Employee updateFromDTO(Employee employee, EmployeeDTO dto) {
        validateEmployeeDTO(dto);
        User user = employee.getUser();
        updateUserFromDTO(user, dto);
        return employee;
    }
    
    private void updateUserFromDTO(User user, EmployeeDTO dto) {
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            userService.updatePassword(user.getId(), dto.getPassword());
        }
    }
} 