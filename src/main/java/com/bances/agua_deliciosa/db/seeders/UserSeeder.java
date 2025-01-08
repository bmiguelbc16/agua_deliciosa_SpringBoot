package com.bances.agua_deliciosa.db.seeders;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;

import com.bances.agua_deliciosa.model.Client;
import com.bances.agua_deliciosa.model.Employee;
import com.bances.agua_deliciosa.model.Gender;
import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.ClientRepository;
import com.bances.agua_deliciosa.repository.EmployeeRepository;
import com.bances.agua_deliciosa.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(3)  // Se ejecutará TERCERO
@RequiredArgsConstructor
public class UserSeeder implements Seeder {

    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean shouldSeed() {
        String sql = "SELECT COUNT(*) FROM users";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count == null || count == 0;
    }

    @Override
    @Transactional
    public void seed() {
        log.info("Iniciando UserSeeder...");
        
        // Verificar si ya existen usuarios
        if (!shouldSeed()) {
            log.info("La tabla users ya contiene datos. Saltando UserSeeder.");
            return;
        }

        // Obtener roles necesarios
        Role adminRole = roleRepository.findByName("Admin")
                .orElseThrow(() -> new RuntimeException("Rol Admin no encontrado"));
        Role employeeRole = roleRepository.findByName("Employee")
                .orElseThrow(() -> new RuntimeException("Rol Employee no encontrado"));
        Role clientRole = roleRepository.findByName("Client")
                .orElseThrow(() -> new RuntimeException("Rol Client no encontrado"));

        // Crear usuarios en orden específico para mantener IDs consistentes
        createAdminUser(adminRole);
        createEmployeeUser(employeeRole);
        createClientUser(clientRole);

        log.info("UserSeeder completado exitosamente.");
    }

    private void createAdminUser(Role adminRole) {
        // Crear y configurar el User para Admin (ID 1)
        User adminUser = new User();
        adminUser.setName("Admin");
        adminUser.setLastName("System");
        adminUser.setEmail("admin@aguadeliciosa.com");
        adminUser.setDocumentNumber("00000001");
        adminUser.setPassword(passwordEncoder.encode("admin123"));
        adminUser.setGender(Gender.MALE);
        adminUser.setBirthDate(LocalDate.of(1990, 1, 1));
        adminUser.setPhoneNumber("999999999");
        adminUser.setActive(true);
        adminUser.setRoleId(adminRole.getId());
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setUpdatedAt(LocalDateTime.now());
        
        // Crear Employee (ID 1)
        Employee employee = new Employee();
        employee.setUser(adminUser);
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());
        
        employeeRepository.save(employee);
        log.info("Usuario administrador creado: {}", adminUser.getEmail());
    }

    private void createEmployeeUser(Role employeeRole) {
        // Crear y configurar el User para Employee (ID 2)
        User employeeUser = new User();
        employeeUser.setName("John");
        employeeUser.setLastName("Doe");
        employeeUser.setEmail("employee@aguadeliciosa.com");
        employeeUser.setDocumentNumber("00000002");
        employeeUser.setPassword(passwordEncoder.encode("employee123"));
        employeeUser.setGender(Gender.MALE);
        employeeUser.setBirthDate(LocalDate.of(1995, 1, 1));
        employeeUser.setPhoneNumber("988888888");
        employeeUser.setActive(true);
        employeeUser.setRoleId(employeeRole.getId());
        employeeUser.setCreatedAt(LocalDateTime.now());
        employeeUser.setUpdatedAt(LocalDateTime.now());
        
        // Crear Employee (ID 2)
        Employee employee = new Employee();
        employee.setUser(employeeUser);
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());
        
        employeeRepository.save(employee);
        log.info("Usuario empleado creado: {}", employeeUser.getEmail());
    }

    private void createClientUser(Role clientRole) {
        // Crear y configurar el User para Client (ID 3)
        User clientUser = new User();
        clientUser.setName("Jane");
        clientUser.setLastName("Doe");
        clientUser.setEmail("client@aguadeliciosa.com");
        clientUser.setDocumentNumber("00000003");
        clientUser.setPassword(passwordEncoder.encode("client123"));
        clientUser.setGender(Gender.FEMALE); // Cambio de género a FEMALE
        clientUser.setBirthDate(LocalDate.of(2000, 1, 1));
        clientUser.setPhoneNumber("977777777");
        clientUser.setActive(true);
        clientUser.setRoleId(clientRole.getId());
        clientUser.setCreatedAt(LocalDateTime.now());
        clientUser.setUpdatedAt(LocalDateTime.now());
        
        // Crear Client (ID 1)
        Client client = new Client();
        client.setUser(clientUser);
        client.setCreatedAt(LocalDateTime.now());
        client.setUpdatedAt(LocalDateTime.now());
        
        clientRepository.save(client);
        log.info("Usuario cliente creado: {}", clientUser.getEmail());
    }
}
