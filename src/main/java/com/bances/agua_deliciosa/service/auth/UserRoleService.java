package com.bances.agua_deliciosa.service.auth;

import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.RoleRepository;
import com.bances.agua_deliciosa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<String> getRoles(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        return roleRepository.findById(user.getRoleId())
            .map(Role::getName)
            .map(List::of)
            .orElse(List.of());
    }
}
