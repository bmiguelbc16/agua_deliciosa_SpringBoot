package com.bances.agua_deliciosa.service.core;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bances.agua_deliciosa.repository.RoleRepository;
import com.bances.agua_deliciosa.model.Role;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoleService {
    
    private final RoleRepository roleRepository;
    
    public List<Role> listAll() {
        return roleRepository.findAll();
    }
    
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }
    
    public Role getByName(String name) {
        return findByName(name)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + name));
    }
    
    @Transactional
    public Role create(String name) {
        if (roleRepository.findByName(name).isPresent()) {
            throw new RuntimeException("El rol ya existe: " + name);
        }
        
        Role role = new Role();
        role.setName(name);
        return roleRepository.save(role);
    }
} 