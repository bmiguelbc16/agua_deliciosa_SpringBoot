package com.bances.agua_deliciosa.db.seeders;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;

import com.bances.agua_deliciosa.model.Employee;
import com.bances.agua_deliciosa.model.Gender;
import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.EmployeeRepository;
import com.bances.agua_deliciosa.repository.RoleRepository;
import com.bances.agua_deliciosa.repository.UserRepository;

@Component
@Order(2)  // Se ejecutará después de RoleSeeder
public class UserSeeder implements Seeder {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean shouldSeed() {
        return userRepository.count() == 0 && employeeRepository.count() == 0;
    }

    @Override
    public void seed() {
        if (shouldSeed()) {
            createAdminUser();
        } else {
            System.out.println("Las tablas de empleados y usuarios ya contienen datos");
        }
    }

    private void createAdminUser() {
        try {
            // 1. Crear el empleado primero (solo con los campos que corresponden)
            Employee employee = new Employee();
            // El empleado solo necesita ser creado, los datos del usuario irán en la tabla user
            employee = employeeRepository.save(employee);

            // 2. Buscar el rol de administrador
            Role adminRole = roleRepository.findByName("Admin")
                    .orElseThrow(() -> new RuntimeException("Rol Admin no encontrado"));

            // 3. Crear el usuario con todos los datos
            User user = new User();
            user.setName("Admin");
            user.setLastName("System");
            user.setEmail("admin@sistema.com");
            user.setDocumentNumber("12345678");
            user.setBirthDate(LocalDate.of(1990, 1, 1));
            user.setGender(Gender.M);
            user.setPhoneNumber("987654321");
            user.setPassword(passwordEncoder.encode("password"));
            user.setRole(adminRole);
            user.setUserableType("Employee");
            user.setUserableId(employee.getId());
            user.setActive(true);
            user.setGuardName("web");  // Valor por defecto según la migración
            
            userRepository.save(user);
            
            System.out.println("Usuario administrador creado exitosamente");
        } catch (Exception e) {
            System.err.println("Error al crear el usuario administrador: " + e.getMessage());
            throw e;
        }
    }
}
