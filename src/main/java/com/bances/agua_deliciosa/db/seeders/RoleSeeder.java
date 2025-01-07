package com.bances.agua_deliciosa.db.seeders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.annotation.Order;

import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Component
@Order(1)  // Se ejecutará PRIMERO
@RequiredArgsConstructor
public class RoleSeeder implements Seeder {

    private final RoleRepository roleRepository;

    @Override
    public boolean shouldSeed() {
        return roleRepository.count() == 0;
    }

    @Override
    @Transactional
    public void seed() {
        List<Role> roles = Arrays.asList(
            createRole("Admin", "Administrador del sistema"),
            createRole("Employee", "Empleado de la empresa"),
            createRole("Client", "Cliente registrado")
        );

        roleRepository.saveAll(roles);
    }

    private Role createRole(String name, String description) {
        Role role = new Role();
        role.setName(name);
        role.setDescription(description);
        role.setCreatedAt(LocalDateTime.now());
        role.setUpdatedAt(LocalDateTime.now());
        role.setActive(true);
        return role;
    }
}
