package com.bances.agua_deliciosa.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.bances.agua_deliciosa.repository.RoleRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoleService {
    
    @Autowired
    private RoleRepository roleRepository;
    
    public List<Map<String, String>> getAvailableRoles() {
        return roleRepository.findAll().stream()
            .filter(role -> !role.getName().equals("ROLE_CLIENTE"))
            .map(role -> Map.of(
                "value", role.getName(),
                "label", role.getName().replace("ROLE_", "")
            ))
            .collect(Collectors.toList());
    }
} 