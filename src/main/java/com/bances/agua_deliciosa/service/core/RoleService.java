package com.bances.agua_deliciosa.service.core;

import java.util.List;
import java.util.stream.Collectors;
import java.util.HashSet;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bances.agua_deliciosa.dto.admin.RoleDTO;
import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.model.Permission;
import com.bances.agua_deliciosa.repository.RoleRepository;
import com.bances.agua_deliciosa.repository.PermissionRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoleService {
    
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public List<RoleDTO> listAll() {
        return roleRepository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public RoleDTO getById(Long id) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        return toDTO(role);
    }

    public Role getByName(String name) {
        return roleRepository.findByName(name)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + name));
    }

    @Transactional
    public RoleDTO create(RoleDTO dto) {
        if (roleRepository.findByName(dto.getName()).isPresent()) {
            throw new RuntimeException("El rol ya existe: " + dto.getName());
        }

        Role role = new Role();
        updateRoleFromDTO(role, dto);
        role = roleRepository.save(role);
        return toDTO(role);
    }

    @Transactional
    public RoleDTO update(Long id, RoleDTO dto) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        
        if (!role.getName().equals(dto.getName()) && 
            roleRepository.findByName(dto.getName()).isPresent()) {
            throw new RuntimeException("El rol ya existe: " + dto.getName());
        }
        
        updateRoleFromDTO(role, dto);
        role = roleRepository.save(role);
        return toDTO(role);
    }

    @Transactional
    public void delete(Long id) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        
        if (role.getName().equals("Administrador")) {
            throw new RuntimeException("No se puede eliminar el rol de Administrador");
        }
        
        roleRepository.delete(role);
    }

    private void updateRoleFromDTO(Role role, RoleDTO dto) {
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        role.setActive(dto.isActive());
        
        if (dto.getPermissionIds() != null && !dto.getPermissionIds().isEmpty()) {
            role.setPermissions(new HashSet<>(
                permissionRepository.findAllById(dto.getPermissionIds())
            ));
        }
    }

    private RoleDTO toDTO(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setDescription(role.getDescription());
        dto.setActive(role.isActive());
        
        if (role.getPermissions() != null) {
            dto.setPermissionIds(role.getPermissions().stream()
                .map(Permission::getId)
                .collect(Collectors.toSet()));
        }
        
        return dto;
    }
}
