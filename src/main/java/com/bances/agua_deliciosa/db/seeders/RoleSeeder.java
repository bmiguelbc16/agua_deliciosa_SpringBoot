package com.bances.agua_deliciosa.db.seeders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;

import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(1)  // Se ejecutará PRIMERO
@RequiredArgsConstructor
public class RoleSeeder implements Seeder {

    private final RoleRepository roleRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean shouldSeed() {
        String sql = "SELECT COUNT(*) FROM roles";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count == null || count == 0;
    }

    @Override
    @Transactional
    public void seed() {
        log.info("Iniciando RoleSeeder...");
        
        // Verificar si ya existen roles
        if (!shouldSeed()) {
            log.info("La tabla roles ya contiene datos. Saltando RoleSeeder.");
            return;
        }

        List<Role> roles = Arrays.asList(
            createRole("Admin", "Administrador del sistema con acceso total"),
            createRole("Employee", "Empleado con acceso a gestión de pedidos y clientes"),
            createRole("Client", "Cliente con acceso a realizar pedidos")
        );

        roles.forEach(role -> {
            roleRepository.save(role);
            log.info("Role creado: {} con ID: {}", role.getName(), role.getId());
        });
        
        log.info("RoleSeeder completado exitosamente.");
    }

    private Role createRole(String name, String description) {
        Role role = new Role();
        role.setName(name);
        role.setDescription(description);
        role.setGuardName("web");
        role.setCreatedAt(LocalDateTime.now());
        role.setUpdatedAt(LocalDateTime.now());
        return role;
    }
}
