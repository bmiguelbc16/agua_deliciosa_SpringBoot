package com.bances.agua_deliciosa.db.seeders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;

import com.bances.agua_deliciosa.model.Permission;
import com.bances.agua_deliciosa.repository.PermissionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(2)  // Se ejecutará SEGUNDO
@RequiredArgsConstructor
public class PermissionSeeder implements Seeder {

    private final PermissionRepository permissionRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean shouldSeed() {
        String sql = "SELECT COUNT(*) FROM permissions";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count == null || count == 0;
    }

    @Override
    @Transactional
    public void seed() {
        log.info("Iniciando PermissionSeeder...");
        
        // Verificar si ya existen permisos
        if (!shouldSeed()) {
            log.info("La tabla permissions ya contiene datos. Saltando PermissionSeeder.");
            return;
        }

        List<Permission> permissions = Arrays.asList(
            createPermission("create:users", "Crear usuarios"),
            createPermission("read:users", "Ver usuarios"),
            createPermission("update:users", "Actualizar usuarios"),
            createPermission("delete:users", "Eliminar usuarios"),
            
            createPermission("create:orders", "Crear pedidos"),
            createPermission("read:orders", "Ver pedidos"),
            createPermission("update:orders", "Actualizar pedidos"),
            createPermission("delete:orders", "Eliminar pedidos"),
            
            createPermission("create:products", "Crear productos"),
            createPermission("read:products", "Ver productos"),
            createPermission("update:products", "Actualizar productos"),
            createPermission("delete:products", "Eliminar productos")
        );

        permissions.forEach(permission -> {
            permissionRepository.save(permission);
            log.info("Permission creado: {} con ID: {}", permission.getName(), permission.getId());
        });
        
        log.info("PermissionSeeder completado exitosamente.");
    }

    private Permission createPermission(String name, String description) {
        Permission permission = new Permission();
        permission.setName(name);
        permission.setDescription(description);
        permission.setGuardName("web");
        permission.setCreatedAt(LocalDateTime.now());
        permission.setUpdatedAt(LocalDateTime.now());
        return permission;
    }
}