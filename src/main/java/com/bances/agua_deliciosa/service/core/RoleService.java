package com.bances.agua_deliciosa.service.core;

import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.repository.RoleRepository;
import com.bances.agua_deliciosa.dto.admin.RoleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoleService {
    
    private final RoleRepository roleRepository;
    
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public List<Role> listAll() {
        return roleRepository.findAll();
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + name));
    }

    public Role getById(Long id) {
        return roleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
    }

    @Transactional
    public Role create(RoleDTO dto) {
        if (roleRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Ya existe un rol con ese nombre");
        }

        Role role = new Role();
        role.setName(dto.getName());
        role.setCreatedAt(LocalDateTime.now());
        
        return roleRepository.save(role);
    }

    @Transactional
    public Role update(Long id, RoleDTO dto) {
        Role role = getById(id);
        
        if (!role.getName().equals(dto.getName()) && 
            roleRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Ya existe un rol con ese nombre");
        }

        role.setName(dto.getName());
        role.setUpdatedAt(LocalDateTime.now());
        
        return roleRepository.save(role);
    }

    @Transactional
    public void delete(Long id) {
        Role role = getById(id);
        
        // Verificar si hay usuarios usando este rol
        if (role.getUsers() != null && !role.getUsers().isEmpty()) {
            throw new RuntimeException("No se puede eliminar el rol porque está siendo usado por usuarios");
        }
        
        roleRepository.delete(role);
    }
}
