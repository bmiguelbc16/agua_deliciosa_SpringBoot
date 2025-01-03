package com.bances.agua_deliciosa.db.seeders;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements Seeder {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public boolean seed() {
        if (alreadySeeded()) {
            System.out.println("Las tablas de roles ya contienen datos");
            return true;
        }

        try {
            System.out.println("Iniciando seed de roles...");
            createRole("Admin", "Acceso total al sistema");
            createRole("Vendedor", "Gestión de ventas y clientes");
            createRole("Almacen", "Gestión de inventario");
            createRole("Gestor", "Gestión de pedidos");
            createRole("Cliente", "Acceso a pedidos personales");
            System.out.println("Seed de roles completado exitosamente");
            return true;
        } catch (Exception e) {
            System.err.println("Error en RoleSeeder: " + e.getMessage());
            return false;
        }
    }

    private Role createRole(String name, String description) {
        Role role = new Role();
        role.setName(name);
        role.setDescription(description);
        role.setGuardName("web");
        role.setCreatedAt(LocalDateTime.now());
        role.setUpdatedAt(LocalDateTime.now());
        Role savedRole = roleRepository.save(role);
        System.out.println("Rol creado: " + name);
        return savedRole;
    }

    @Override
    public boolean alreadySeeded() {
        return roleRepository.count() > 0;
    }
}
