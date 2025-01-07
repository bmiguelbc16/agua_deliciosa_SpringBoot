package com.bances.agua_deliciosa.db.seeders;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.annotation.Order;

import com.bances.agua_deliciosa.model.Permission;
import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.repository.PermissionRepository;
import com.bances.agua_deliciosa.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

@Component
@Order(2)  // Se ejecutará DESPUÉS de RoleSeeder
@RequiredArgsConstructor
public class PermissionSeeder implements Seeder {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    @Override
    public boolean shouldSeed() {
        return permissionRepository.count() == 0;
    }

    @Override
    @Transactional
    public void seed() {
        // Crear permisos básicos
        List<Permission> permissions = Arrays.asList(
            // Permisos de usuarios
            createPermission("users.view", "Ver usuarios"),
            createPermission("users.create", "Crear usuarios"),
            createPermission("users.edit", "Editar usuarios"),
            createPermission("users.delete", "Eliminar usuarios"),
            
            // Permisos de productos
            createPermission("products.view", "Ver productos"),
            createPermission("products.create", "Crear productos"),
            createPermission("products.edit", "Editar productos"),
            createPermission("products.delete", "Eliminar productos"),
            
            // Permisos de pedidos
            createPermission("orders.view", "Ver pedidos"),
            createPermission("orders.create", "Crear pedidos"),
            createPermission("orders.edit", "Editar pedidos"),
            createPermission("orders.delete", "Eliminar pedidos"),
            
            // Permisos de inventario
            createPermission("inventory.view", "Ver inventario"),
            createPermission("inventory.manage", "Gestionar inventario"),
            
            // Permisos de reportes
            createPermission("reports.view", "Ver reportes"),
            createPermission("reports.generate", "Generar reportes")
        );

        permissionRepository.saveAll(permissions);

        // Asignar permisos a roles
        Role adminRole = roleRepository.findByName("Admin")
            .orElseThrow(() -> new RuntimeException("Rol Admin no encontrado"));
        
        Role employeeRole = roleRepository.findByName("Employee")
            .orElseThrow(() -> new RuntimeException("Rol Employee no encontrado"));
        
        Role clientRole = roleRepository.findByName("Client")
            .orElseThrow(() -> new RuntimeException("Rol Client no encontrado"));

        // Admin tiene todos los permisos
        adminRole.setPermissions(new HashSet<>(permissions));
        roleRepository.save(adminRole);

        // Employee tiene permisos limitados
        Set<Permission> employeePermissions = permissions.stream()
            .filter(p -> !p.getName().endsWith(".delete") && 
                        !p.getName().startsWith("users.") &&
                        !p.getName().equals("reports.generate"))
            .collect(java.util.stream.Collectors.toSet());
        employeeRole.setPermissions(employeePermissions);
        roleRepository.save(employeeRole);

        // Client solo puede ver productos y gestionar sus propios pedidos
        Set<Permission> clientPermissions = permissions.stream()
            .filter(p -> p.getName().equals("products.view") || 
                        p.getName().equals("orders.view") ||
                        p.getName().equals("orders.create"))
            .collect(java.util.stream.Collectors.toSet());
        clientRole.setPermissions(clientPermissions);
        roleRepository.save(clientRole);
    }

    private Permission createPermission(String name, String description) {
        Permission permission = new Permission();
        permission.setName(name);
        permission.setDescription(description);
        permission.setCreatedAt(LocalDateTime.now());
        permission.setUpdatedAt(LocalDateTime.now());
        permission.setActive(true);
        return permission;
    }
}