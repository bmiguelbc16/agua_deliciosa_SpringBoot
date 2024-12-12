package com.bances.agua_deliciosa.db.seeders;

import org.springframework.stereotype.Component;
import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements Seeder {
    
    private final RoleRepository roleRepository;
    
    @Override
    public void seed() {
        createRole("Admin", "Acceso total al sistema");
        createRole("Gestor de pedidos", "Gestión de pedidos y entregas");
        createRole("Almacen", "Gestión de inventario y stock");
        createRole("Vendedor", "Registro de ventas y clientes");
        createRole("Cliente", "Acceso a pedidos personales");
    }
    
    private Role createRole(String name, String description) {
        return roleRepository.findByName(name)
            .orElseGet(() -> {
                Role role = new Role();
                role.setName(name);
                role.setDescription(description);
                role.setGuardName("web");
                role.setCreatedAt(LocalDateTime.now());
                role.setUpdatedAt(LocalDateTime.now());
                return roleRepository.save(role);
            });
    }
} 