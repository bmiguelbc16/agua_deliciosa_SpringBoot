package com.bances.agua_deliciosa.service.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bances.agua_deliciosa.model.Permission;
import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.repository.PermissionRepository;
import com.bances.agua_deliciosa.repository.RoleRepository;
import com.bances.agua_deliciosa.repository.UserRepository;

import java.time.LocalDateTime;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public Role findById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Role findByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Role not found with name: " + name));
    }

    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Transactional
    public Role save(Role role) {
        validateRoleName(role);
        role.setCreatedAt(LocalDateTime.now());
        return roleRepository.save(role);
    }

    @Transactional
    public Role update(Role role) {
        Role existingRole = findById(role.getId());
        
        if (!existingRole.getName().equals(role.getName())) {
            validateRoleName(role);
        }
        role.setUpdatedAt(LocalDateTime.now());
        return roleRepository.save(role);
    }

    @Transactional
    public void delete(Long id) {
        findById(id); // Verificar que existe
        if (userRepository.countByRoleId(id) > 0) {
            throw new RuntimeException("Cannot delete role with associated users");
        }
        roleRepository.delete(id);
    }

    @Transactional
    public void assignPermission(Long roleId, Long permissionId) {
        Role role = findById(roleId);
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        
        role.getPermissions().add(permission);
        roleRepository.save(role);
    }

    @Transactional
    public void removePermission(Long roleId, Long permissionId) {
        Role role = findById(roleId);
        role.getPermissions().removeIf(p -> p.getId().equals(permissionId));
        roleRepository.save(role);
    }

    private void validateRoleName(Role role) {
        roleRepository.findByName(role.getName())
                .ifPresent(existingRole -> {
                    if (!existingRole.getId().equals(role.getId())) {
                        throw new RuntimeException("Role name already exists");
                    }
                });
    }
}
