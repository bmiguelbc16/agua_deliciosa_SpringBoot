package com.bances.agua_deliciosa.db.seeders;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
import com.bances.agua_deliciosa.repository.ClientRepository;
import com.bances.agua_deliciosa.repository.EmployeeRepository;
import com.bances.agua_deliciosa.repository.RoleRepository;
import com.bances.agua_deliciosa.repository.UserRepository;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(2) // Se ejecuta después de RoleSeeder que tiene @Order(1)
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
        try {
            log.info("Iniciando seed de usuarios...");
            log.info("Buscando roles necesarios...");

            Role adminRole = roleRepository.findByName("Admin")
                .orElseThrow(() -> new RuntimeException("Rol Admin no encontrado"));
            log.info("Rol Admin encontrado: {}", adminRole.getId());

            Role clientRole = roleRepository.findByName("Cliente")
                .orElseThrow(() -> new RuntimeException("Rol Cliente no encontrado"));
            log.info("Rol Cliente encontrado: {}", clientRole.getId());

            createAdminUser(adminRole);
            createClientUser(clientRole);

            log.info("Seed de usuarios completado exitosamente");
        } catch (Exception e) {
            log.error("Error en UserSeeder: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void createAdminUser(Role adminRole) {
        try {
            log.info("Creando usuario administrador...");
            
            Employee employee = new Employee();
            LocalDateTime now = LocalDateTime.now();
            employee.setCreatedAt(now);
            employee.setUpdatedAt(now);
            employee = employeeRepository.save(employee);
            
            User adminUser = new User();
            adminUser.setName("Admin");
            adminUser.setLastName("System");
            adminUser.setEmail("admin@sistema.com");
            adminUser.setDocumentNumber("12345678");
            adminUser.setPassword(passwordEncoder.encode("password"));
            adminUser.setBirthDate(LocalDate.of(1990, 1, 1));
            adminUser.setGender(Gender.MALE);
            adminUser.setPhoneNumber("123456789");
            adminUser.setRole(adminRole);
            adminUser.setActive(true);
            adminUser.setUserableId(employee.getId());
            adminUser.setUserableType("Employee");
            adminUser.setCreatedAt(now);
            adminUser.setUpdatedAt(now);
            adminUser = userRepository.save(adminUser);
            log.info("Usuario administrador creado con ID: {}", adminUser.getId());
        } catch (Exception e) {
            log.error("Error creando usuario administrador: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void createClientUser(Role clientRole) {
        try {
            log.info("Creando usuario cliente...");
            
            // Primero creamos el Client base
            Client client = new Client();
            LocalDateTime now = LocalDateTime.now();
            client.setCreatedAt(now);
            client.setUpdatedAt(now);
            client = clientRepository.save(client);
            
            // Luego creamos el User con la relación polimórfica
            User clientUser = new User();
            clientUser.setName("Client");
            clientUser.setLastName("User");
            clientUser.setEmail("client@example.com");
            clientUser.setDocumentNumber("87654321");
            clientUser.setPassword(passwordEncoder.encode("password"));
            clientUser.setBirthDate(LocalDate.of(1995, 1, 1));
            clientUser.setGender(Gender.FEMALE);
            clientUser.setPhoneNumber("987654321");
            clientUser.setRole(clientRole);
            clientUser.setActive(true);
            clientUser.setUserableId(client.getId());
            clientUser.setUserableType("Client");
            clientUser.setCreatedAt(now);
            clientUser.setUpdatedAt(now);
            clientUser = userRepository.save(clientUser);
            
            log.info("Usuario cliente creado con ID: {}", clientUser.getId());
        } catch (Exception e) {
            log.error("Error creando usuario cliente: {}", e.getMessage(), e);
            throw e;
        }
    }
}
