package com.bances.agua_deliciosa.db.seeders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bances.agua_deliciosa.model.Employee;
import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.EmployeeRepository;
import com.bances.agua_deliciosa.repository.RoleRepository;
import com.bances.agua_deliciosa.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserSeeder implements Seeder {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public boolean seed() {
        if (alreadySeeded()) {
            System.out.println("Las tablas de usuarios ya contienen datos");
            return true;
        }

        try {
            System.out.println("Iniciando seed de usuarios...");

            // 1. Crear Employee primero
            Employee employee = new Employee();
            employee.setCreatedAt(LocalDateTime.now());
            employee.setUpdatedAt(LocalDateTime.now());
            employee = employeeRepository.save(employee);
            System.out.println("Employee creado con ID: " + employee.getId());

            // 2. Obtener Rol Admin
            Role adminRole = roleRepository.findByName("Admin")
                .orElseThrow(() -> new RuntimeException("Role Admin not found"));

            // 3. Crear User
            User admin = new User();
            admin.setName("Admin");
            admin.setLastName("Sistema");
            admin.setEmail("admin@sistema.com");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setDocumentNumber("00000000");
            admin.setBirthDate(LocalDate.of(1990, 1, 1));
            admin.setGender("M");
            admin.setPhoneNumber("987654321");
            admin.setUserableType("Employee");
            admin.setUserableId(employee.getId());
            admin.setEmailVerifiedAt(LocalDateTime.now());
            admin.setActive(true);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());
            admin.setRole(adminRole); // Asignar el rol directamente

            // 4. Guardar User
            admin = userRepository.save(admin);
            System.out.println("Usuario admin creado exitosamente con ID: " + admin.getId());

            return true;
        } catch (Exception e) {
            System.err.println("Error en UserSeeder: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean alreadySeeded() {
        return userRepository.count() > 0;
    }

    @Override
    public List<Class<? extends Seeder>> getDependencies() {
        return List.of(RoleSeeder.class);
    }
}
