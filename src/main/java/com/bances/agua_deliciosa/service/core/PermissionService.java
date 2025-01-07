package com.bances.agua_deliciosa.service.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bances.agua_deliciosa.model.Permission;
import com.bances.agua_deliciosa.repository.PermissionRepository;
import com.bances.agua_deliciosa.repository.RoleRepository;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Permission findById(Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Permission findByName(String name) {
        return permissionRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Permission not found with name: " + name));
    }

    @Transactional(readOnly = true)
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Transactional
    public Permission save(Permission permission) {
        validatePermissionName(permission);
        return permissionRepository.save(permission);
    }

    @Transactional
    public Permission update(Permission permission) {
        Permission existingPermission = findById(permission.getId());
        
        if (!existingPermission.getName().equals(permission.getName())) {
            validatePermissionName(permission);
        }
        
        return permissionRepository.save(permission);
    }

    @Transactional
    public void delete(Long id) {
        findById(id); // Verificar que existe
        int roleCount = roleRepository.countByPermissionId(id);
        
        if (roleCount > 0) {
            throw new RuntimeException("Cannot delete permission that is being used by roles");
        }
        
        permissionRepository.delete(id);
    }

    private void validatePermissionName(Permission permission) {
        permissionRepository.findByName(permission.getName())
                .ifPresent(existingPermission -> {
                    if (!existingPermission.getId().equals(permission.getId())) {
                        throw new RuntimeException("Permission name already exists");
                    }
                });
    }
}
