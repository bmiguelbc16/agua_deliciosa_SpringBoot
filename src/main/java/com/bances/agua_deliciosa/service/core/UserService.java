package com.bances.agua_deliciosa.service.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.RoleRepository;
import com.bances.agua_deliciosa.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @Transactional
    public void assignRole(User user, String roleName) {
        Role currentRole = user.getRoles().isEmpty() ? null : user.getRoles().iterator().next();
        
        if (currentRole != null && currentRole.getName().equals(roleName)) {
            return;
        }

        Role newRole = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
        
        user.getRoles().clear();
        user.getRoles().add(newRole);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getProfile(String email) {
        return findByEmail(email);
    }

    @Transactional
    public void updateProfile(User user) {
        if (user.getEmail() != null && userRepository.existsByEmailAndIdNot(user.getEmail(), user.getId())) {
            throw new RuntimeException("Email already registered");
        }

        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(user);
    }

    @Transactional
    public User createUser(User user) {
        if (user.getPassword() == null) {
            throw new RuntimeException("Password is required");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
