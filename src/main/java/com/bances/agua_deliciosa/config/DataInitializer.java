package com.bances.agua_deliciosa.config;

import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.RoleRepository;
import com.bances.agua_deliciosa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Crear roles si no existen
        if (roleRepository.count() == 0) {
            createRole("ROLE_ADMIN", "Administrador del sistema");
            createRole("ROLE_GERENTE", "Gerente del sistema");
            createRole("ROLE_VENDEDOR", "Vendedor");
            createRole("ROLE_REPARTIDOR", "Repartidor");
            createRole("ROLE_CLIENTE", "Cliente");
        }

        // Crear usuario admin si no existe
        if (!userRepository.findByUsername("admin").isPresent()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@aguadeliciosa.com");
            admin.setRoles(Collections.singleton(
                roleRepository.findByName("ROLE_ADMIN").orElseThrow()
            ));
            userRepository.save(admin);
        }
    }

    private void createRole(String name, String description) {
        Role role = new Role();
        role.setName(name);
        role.setDescription(description);
        roleRepository.save(role);
    }
} 