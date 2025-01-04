package com.bances.agua_deliciosa.service.core;

import com.bances.agua_deliciosa.model.Permission;
import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.repository.PermissionRepository;
import com.bances.agua_deliciosa.dto.admin.PermissionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PermissionService {
    
    private final PermissionRepository permissionRepository;

    public Set<Permission> getAllPermissions() {
        return new HashSet<>(permissionRepository.findAll());
    }

    public Set<Permission> getEmployeePermissions() {
        List<Permission> allPermissions = permissionRepository.findAll();
        return allPermissions.stream()
            .filter(permission -> !permission.getName().startsWith("admin"))
            .collect(Collectors.toSet());
    }

    public Set<Permission> getClientPermissions() {
        List<Permission> allPermissions = permissionRepository.findAll();
        return allPermissions.stream()
            .filter(permission -> permission.getName().startsWith("client"))
            .collect(Collectors.toSet());
    }

    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public Permission findByName(String name) {
        return permissionRepository.findByName(name)
            .orElseThrow(() -> new RuntimeException("Permiso no encontrado: " + name));
    }

    public Permission getPermissionById(Long id) {
        return permissionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Permiso no encontrado con ID: " + id));
    }

    @Transactional
    public Permission createPermission(PermissionDTO dto) {
        if (permissionRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Ya existe un permiso con ese nombre");
        }

        Permission permission = new Permission();
        permission.setName(dto.getName());
        permission.setDescription(dto.getDescription());

        return permissionRepository.save(permission);
    }

    @Transactional
    public Permission update(Long id, PermissionDTO dto) {
        Permission permission = getPermissionById(id);

        if (!permission.getName().equals(dto.getName()) && 
            permissionRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Ya existe un permiso con ese nombre");
        }

        permission.setName(dto.getName());
        permission.setDescription(dto.getDescription());

        return permissionRepository.save(permission);
    }

    @Transactional
    public void deletePermission(Long id) {
        Permission permission = getPermissionById(id);

        // Verificar si el permiso está siendo usado por algún rol
        boolean isUsedByRole = permissionRepository.findRolesUsingPermission(id).size() > 0;
        
        if (isUsedByRole) {
            throw new RuntimeException("No se puede eliminar el permiso porque está siendo usado por roles");
        }

        permissionRepository.delete(permission);
    }

    public Set<String> getRolePermissions(Role role) {
        return role.getPermissions().stream()
            .map(Permission::getName)
            .collect(Collectors.toSet());
    }
}
