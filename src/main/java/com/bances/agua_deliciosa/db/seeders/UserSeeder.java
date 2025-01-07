package com.bances.agua_deliciosa.db.seeders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.annotation.Order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.bances.agua_deliciosa.model.Client;
import com.bances.agua_deliciosa.model.Employee;
import com.bances.agua_deliciosa.model.Gender;
import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.model.UserVerification;
import com.bances.agua_deliciosa.repository.ClientRepository;
import com.bances.agua_deliciosa.repository.EmployeeRepository;
import com.bances.agua_deliciosa.repository.RoleRepository;
import com.bances.agua_deliciosa.repository.UserRepository;

@Slf4j
@Component
@Order(3)  // Se ejecutará DESPUÉS de PermissionSeeder
@RequiredArgsConstructor
public class UserSeeder implements Seeder {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean shouldSeed() {
        return userRepository.count() == 0;
    }

    @Override
    @Transactional
    public void seed() {
        // Obtener roles
        Role adminRole = roleRepository.findByName("Admin")
            .orElseThrow(() -> new RuntimeException("Rol Admin no encontrado"));
        
        Role employeeRole = roleRepository.findByName("Employee")
            .orElseThrow(() -> new RuntimeException("Rol Employee no encontrado"));
        
        Role clientRole = roleRepository.findByName("Client")
            .orElseThrow(() -> new RuntimeException("Rol Client no encontrado"));

        // Crear usuarios base
        createAdminUser(adminRole);
        createEmployeeUser(employeeRole);
        createClientUser(clientRole);
    }

    private void createAdminUser(Role adminRole) {
        Employee employee = new Employee();
        employee.setFirstName("Admin");
        employee.setLastName("System");
        employee.setGender(Gender.MALE);
        employee.setBirthDate(LocalDate.of(1990, 1, 1));
        employee.setPhone("999999999");
        employee.setAddress("Dirección Administrativa");
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());
        employee.setActive(true);
        employee = employeeRepository.save(employee);

        User adminUser = new User();
        adminUser.setEmail("admin@aguadeliciosa.com");
        adminUser.setPassword(passwordEncoder.encode("admin123"));
        adminUser.setUserableType("Employee");
        adminUser.setUserableId(employee.getId());
        adminUser.setRoles(new HashSet<>(Collections.singletonList(adminRole)));
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setUpdatedAt(LocalDateTime.now());
        adminUser.setActive(true);

        // Crear verificación
        UserVerification verification = new UserVerification();
        verification.setToken("admin-verified");
        verification.setVerifiedAt(LocalDateTime.now());
        verification.setExpiresAt(LocalDateTime.now().plusYears(1));
        verification.setActive(true);
        verification.setCreatedAt(LocalDateTime.now());
        verification.setUpdatedAt(LocalDateTime.now());
        
        adminUser.setVerifications(new HashSet<>(Collections.singletonList(verification)));
        verification.setUser(adminUser);
        
        userRepository.save(adminUser);
    }

    private void createEmployeeUser(Role employeeRole) {
        Employee employee = new Employee();
        employee.setFirstName("Empleado");
        employee.setLastName("Demo");
        employee.setGender(Gender.MALE);
        employee.setBirthDate(LocalDate.of(1995, 1, 1));
        employee.setPhone("988888888");
        employee.setAddress("Dirección Empleado");
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());
        employee.setActive(true);
        employee = employeeRepository.save(employee);

        User employeeUser = new User();
        employeeUser.setEmail("empleado@aguadeliciosa.com");
        employeeUser.setPassword(passwordEncoder.encode("empleado123"));
        employeeUser.setUserableType("Employee");
        employeeUser.setUserableId(employee.getId());
        employeeUser.setRoles(new HashSet<>(Collections.singletonList(employeeRole)));
        employeeUser.setCreatedAt(LocalDateTime.now());
        employeeUser.setUpdatedAt(LocalDateTime.now());
        employeeUser.setActive(true);

        // Crear verificación
        UserVerification verification = new UserVerification();
        verification.setToken("employee-verified");
        verification.setVerifiedAt(LocalDateTime.now());
        verification.setExpiresAt(LocalDateTime.now().plusYears(1));
        verification.setActive(true);
        verification.setCreatedAt(LocalDateTime.now());
        verification.setUpdatedAt(LocalDateTime.now());
        
        employeeUser.setVerifications(new HashSet<>(Collections.singletonList(verification)));
        verification.setUser(employeeUser);
        
        userRepository.save(employeeUser);
    }

    private void createClientUser(Role clientRole) {
        Client client = new Client();
        client.setFirstName("Cliente");
        client.setLastName("Demo");
        client.setGender(Gender.MALE);
        client.setBirthDate(LocalDate.of(2000, 1, 1));
        client.setPhone("977777777");
        client.setAddress("Dirección Cliente");
        client.setCreatedAt(LocalDateTime.now());
        client.setUpdatedAt(LocalDateTime.now());
        client.setActive(true);
        client = clientRepository.save(client);

        User clientUser = new User();
        clientUser.setEmail("cliente@demo.com");
        clientUser.setPassword(passwordEncoder.encode("cliente123"));
        clientUser.setUserableType("Client");
        clientUser.setUserableId(client.getId());
        clientUser.setRoles(new HashSet<>(Collections.singletonList(clientRole)));
        clientUser.setCreatedAt(LocalDateTime.now());
        clientUser.setUpdatedAt(LocalDateTime.now());
        clientUser.setActive(true);

        // Crear verificación
        UserVerification verification = new UserVerification();
        verification.setToken("client-verified");
        verification.setVerifiedAt(LocalDateTime.now());
        verification.setExpiresAt(LocalDateTime.now().plusYears(1));
        verification.setActive(true);
        verification.setCreatedAt(LocalDateTime.now());
        verification.setUpdatedAt(LocalDateTime.now());
        
        clientUser.setVerifications(new HashSet<>(Collections.singletonList(verification)));
        verification.setUser(clientUser);
        
        userRepository.save(clientUser);
    }
}
