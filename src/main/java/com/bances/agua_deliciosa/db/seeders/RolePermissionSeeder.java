package com.bances.agua_deliciosa.db.seeders;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;

import com.bances.agua_deliciosa.model.Permission;
import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.repository.PermissionRepository;
import com.bances.agua_deliciosa.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(4)  // Se ejecutará CUARTO
@RequiredArgsConstructor
public class RolePermissionSeeder implements Seeder {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean shouldSeed() {
        String sql = "SELECT COUNT(*) FROM role_has_permissions";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count == null || count == 0;
    }

    @Override
    @Transactional
    public void seed() {
        log.info("Iniciando RolePermissionSeeder...");
        
        // Verificar si ya existen asignaciones
        if (!shouldSeed()) {
            log.info("La tabla role_has_permissions ya contiene datos. Saltando RolePermissionSeeder.");
            return;
        }

        // Obtener todos los roles y permisos
        List<Role> roles = roleRepository.findAll();
        List<Permission> permissions = permissionRepository.findAll();

        if (roles.isEmpty() || permissions.isEmpty()) {
            log.warn("No hay roles o permisos para asignar. Saltando RolePermissionSeeder.");
            return;
        }

        // Asignar permisos al rol Admin (todos los permisos)
        Role adminRole = roles.stream()
                .filter(role -> role.getName().equals("Admin"))
                .findFirst()
                .orElse(null);

        if (adminRole != null) {
            permissions.forEach(permission -> {
                assignPermissionToRole(adminRole.getId(), permission.getId());
                log.info("Asignando permiso {} al rol Admin", permission.getName());
            });
        }

        // Asignar permisos al rol Employee
        Role employeeRole = roles.stream()
                .filter(role -> role.getName().equals("Employee"))
                .findFirst()
                .orElse(null);

        if (employeeRole != null) {
            permissions.stream()
                    .filter(permission -> permission.getName().startsWith("read:") || 
                                        permission.getName().startsWith("create:") || 
                                        permission.getName().startsWith("update:"))
                    .forEach(permission -> {
                        assignPermissionToRole(employeeRole.getId(), permission.getId());
                        log.info("Asignando permiso {} al rol Employee", permission.getName());
                    });
        }

        // Asignar permisos al rol Client
        Role clientRole = roles.stream()
                .filter(role -> role.getName().equals("Client"))
                .findFirst()
                .orElse(null);

        if (clientRole != null) {
            permissions.stream()
                    .filter(permission -> permission.getName().startsWith("read:"))
                    .forEach(permission -> {
                        assignPermissionToRole(clientRole.getId(), permission.getId());
                        log.info("Asignando permiso {} al rol Client", permission.getName());
                    });
        }

        log.info("RolePermissionSeeder completado exitosamente.");
    }

    private void assignPermissionToRole(Long roleId, Long permissionId) {
        String sql = """
            INSERT INTO role_has_permissions (permission_id, role_id)
            VALUES (?, ?)
        """;
        
        jdbcTemplate.update(sql, 
            permissionId,
            roleId
        );
    }
}
