package com.bances.agua_deliciosa.db.seeders;

import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.model.Employee;
import com.bances.agua_deliciosa.model.Client;
import com.bances.agua_deliciosa.repository.RoleRepository;
import com.bances.agua_deliciosa.repository.UserRepository;
import com.bances.agua_deliciosa.repository.EmployeeRepository;
import com.bances.agua_deliciosa.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.bances.agua_deliciosa.model.Gender;

@Component
@RequiredArgsConstructor
public class UserSeeder implements Seeder {
    
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void seed() {
        // Crear empleado admin
        Employee adminEmployee = new Employee();
        adminEmployee.setPosition("Administrador General");
        adminEmployee.setSalary(new BigDecimal("3000.00"));
        adminEmployee.setHireDate(LocalDate.now());
        adminEmployee.setGender(Gender.M);
        adminEmployee = employeeRepository.save(adminEmployee);
        
        // Crear usuario admin
        createUser(
            "Admin",
            "System",
            "admin@example.com",
            "12345678",
            LocalDate.now(),
            Gender.M,
            "999888777",
            adminEmployee.getId(),
            "Employee",
            "Admin"
        );

        // Crear empleado vendedor
        Employee vendorEmployee = new Employee();
        vendorEmployee.setPosition("Vendedor");
        vendorEmployee.setSalary(new BigDecimal("1800.00"));
        vendorEmployee.setHireDate(LocalDate.now());
        vendorEmployee.setGender(Gender.M);
        vendorEmployee = employeeRepository.save(vendorEmployee);
        
        // Crear usuario vendedor
        createUser(
            "Juan",
            "Pérez",
            "vendedor@example.com",
            "87654321",
            LocalDate.of(1995, 1, 15),
            Gender.M,
            "999777666",
            vendorEmployee.getId(),
            "Employee",
            "Vendedor"
        );

        // Crear cliente
        Client client = new Client();
        client.setAddress("Av. Principal 123");
        client.setReference("Cerca al parque central");
        client.setLatitude(-12.0464);
        client.setLongitude(-77.0428);
        client.setGender(Gender.F);
        client = clientRepository.save(client);
        
        // Crear usuario cliente
        createUser(
            "María",
            "García",
            "cliente@example.com",
            "99887766",
            LocalDate.of(1990, 6, 20),
            Gender.F,
            "987654321",
            client.getId(),
            "Client",
            "Cliente"
        );
    }
    
    private User createUser(
        String name,
        String lastName,
        String email,
        String documentNumber,
        LocalDate birthDate,
        Gender gender,
        String phoneNumber,
        Long userableId,
        String userableType,
        String roleName
    ) {
        if (userRepository.findByEmail(email).isPresent()) {
            return userRepository.findByEmail(email).get();
        }

        User user = new User();
        user.setName(name);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setDocumentNumber(documentNumber);
        user.setBirthDate(birthDate);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(passwordEncoder.encode("password"));
        user.setUserableId(userableId);
        user.setUserableType(userableType);
        user.setEmailVerifiedAt(LocalDateTime.now());
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        Role role = roleRepository.findByName(roleName)
            .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
        user.getRoles().add(role);
        
        return userRepository.save(user);
    }
} 