package com.bances.agua_deliciosa.service.core;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bances.agua_deliciosa.repository.PermissionRepository;
import com.bances.agua_deliciosa.model.Permission;
import com.bances.agua_deliciosa.repository.RoleRepository;
import com.bances.agua_deliciosa.model.Role;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    // Obtener todos los permisos
    public List<Permission> listAll() {
        return permissionRepository.findAll();
    }

    // Buscar permiso por nombre
    public Optional<Permission> findByName(String name) {
        return permissionRepository.findByName(name);
    }

    // Obtener un permiso por su ID
    public Permission getById(Long id) {
        return permissionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Permiso no encontrado con id: " + id));
    }

    // Crear un nuevo permiso
    @Transactional
    public Permission create(String name, String description) {
        if (permissionRepository.findByName(name).isPresent()) {
            throw new RuntimeException("El permiso ya existe: " + name);
        }

        Permission permission = new Permission();
        permission.setName(name);
        permission.setDescription(description);
        return permissionRepository.save(permission);
    }

    // Actualizar un permiso
    @Transactional
    public Permission update(Long id, String name, String description) {
        Permission permission = permissionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Permiso no encontrado con id: " + id));
        
        permission.setName(name);
        permission.setDescription(description);
        return permissionRepository.save(permission);
    }

    // Eliminar un permiso
    @Transactional
    public void delete(Long id) {
        Permission permission = permissionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Permiso no encontrado con id: " + id));
        
        permissionRepository.delete(permission);
    }

    // Asignar permiso a un rol
    @Transactional
    public void assignPermissionToRole(Long roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + roleId));
        
        Permission permission = permissionRepository.findById(permissionId)
            .orElseThrow(() -> new RuntimeException("Permiso no encontrado con id: " + permissionId));
        
        role.getPermissions().add(permission);
        roleRepository.save(role); // Guardar el rol con el permiso asignado
    }
}
