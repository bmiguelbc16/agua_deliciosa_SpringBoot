package com.bances.agua_deliciosa.service.core;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bances.agua_deliciosa.repository.PermissionRepository;
import com.bances.agua_deliciosa.model.Permission;
import com.bances.agua_deliciosa.repository.RoleRepository;
import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.dto.admin.PermissionDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public PermissionDTO getPermissionById(Long id) {
        Permission permission = permissionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Permiso no encontrado con id: " + id));
        return toDTO(permission);
    }

    @Transactional
    public PermissionDTO createPermission(PermissionDTO dto) {
        if (permissionRepository.findByName(dto.getName()).isPresent()) {
            throw new RuntimeException("El permiso ya existe: " + dto.getName());
        }

        Permission permission = new Permission();
        updatePermissionFromDTO(permission, dto);
        permission = permissionRepository.save(permission);
        return toDTO(permission);
    }

    @Transactional
    public PermissionDTO update(Long id, PermissionDTO dto) {
        Permission permission = permissionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Permiso no encontrado con id: " + id));
        
        updatePermissionFromDTO(permission, dto);
        permission = permissionRepository.save(permission);
        return toDTO(permission);
    }

    @Transactional
    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Permiso no encontrado con id: " + id));
        
        permissionRepository.delete(permission);
    }

    private void updatePermissionFromDTO(Permission permission, PermissionDTO dto) {
        permission.setName(dto.getName());
        permission.setDescription(dto.getDescription());
        permission.setActive(dto.isActive());
    }

    private PermissionDTO toDTO(Permission permission) {
        PermissionDTO dto = new PermissionDTO();
        dto.setId(permission.getId());
        dto.setName(permission.getName());
        dto.setDescription(permission.getDescription());
        dto.setActive(permission.isActive());
        return dto;
    }

    @Transactional
    public void assignPermissionToRole(Long roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + roleId));
        
        Permission permission = permissionRepository.findById(permissionId)
            .orElseThrow(() -> new RuntimeException("Permiso no encontrado con id: " + permissionId));
        
        role.getPermissions().add(permission);
        roleRepository.save(role);
    }
}
