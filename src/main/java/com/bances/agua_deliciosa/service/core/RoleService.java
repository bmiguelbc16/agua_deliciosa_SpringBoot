package com.bances.agua_deliciosa.service.core;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.repository.RoleRepository;

@Service
public class RoleService {
    
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getByName(String name) {
        return roleRepository.findByName(name)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + name));
    }
}
