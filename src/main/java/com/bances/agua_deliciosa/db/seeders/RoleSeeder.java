package com.bances.agua_deliciosa.db.seeders;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.repository.RoleRepository;
import java.time.LocalDateTime;

@Component
public class RoleSeeder {
    
    @Autowired
    private RoleRepository roleRepository;
    
    public void seed() {
        createRole("ROLE_ADMIN", "Admin");
        createRole("ROLE_GESTOR", "Gestor");
        createRole("ROLE_ALMACEN", "Almacen");
        createRole("ROLE_VENDEDOR", "Vendedor");
        createRole("ROLE_CLIENTE", "Cliente");
    }
    
    private void createRole(String name, String guardName) {
        if (!roleRepository.findByName(name).isPresent()) {
            Role role = new Role();
            role.setName(name);
            role.setGuardName(guardName);
            role.setCreatedAt(LocalDateTime.now());
            role.setUpdatedAt(LocalDateTime.now());
            roleRepository.save(role);
        }
    }
} 