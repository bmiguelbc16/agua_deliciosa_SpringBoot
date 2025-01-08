package com.bances.agua_deliciosa.service.core;

import com.bances.agua_deliciosa.model.Permission;
import com.bances.agua_deliciosa.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    @Transactional(readOnly = true)
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Permission> findById(Long id) {
        return permissionRepository.findById(id);
    }

    @Transactional
    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Transactional
    public void deleteById(Long id) {
        permissionRepository.deleteById(id);
    }
}
