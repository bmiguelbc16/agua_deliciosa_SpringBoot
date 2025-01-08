package com.bances.agua_deliciosa.service.core;

import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    // IDs de roles del sistema
    private static final Long ROLE_CLIENT_ID = 1L;
    private static final Long ROLE_EMPLOYEE_ID = 2L;
    private static final Long ROLE_ADMIN_ID = 3L;

    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Role> findAllById(List<Long> ids) {
        return roleRepository.findByIds(ids);
    }

    @Transactional(readOnly = true)
    public List<Role> findEmployeeRoles() {
        return roleRepository.findByIds(List.of(ROLE_EMPLOYEE_ID, ROLE_ADMIN_ID));
    }

    @Transactional(readOnly = true)
    public List<Role> findClientRoles() {
        return roleRepository.findByIds(List.of(ROLE_CLIENT_ID));
    }

    @Transactional(readOnly = true)
    public Role findClientRole() {
        return roleRepository.findById(ROLE_CLIENT_ID)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @Transactional(readOnly = true)
    public Role findEmployeeRole() {
        return roleRepository.findById(ROLE_EMPLOYEE_ID)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @Transactional(readOnly = true)
    public Role findAdminRole() {
        return roleRepository.findById(ROLE_ADMIN_ID)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @Transactional
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Transactional
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }
}
