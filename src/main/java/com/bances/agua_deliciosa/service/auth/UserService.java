package com.bances.agua_deliciosa.service.auth;

import org.springframework.transaction.annotation.Transactional;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.UserRepository;
import java.util.Optional;

@Transactional(readOnly = true)
public abstract class UserService {
    
    protected final UserRepository userRepository;
    
    protected UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public User getById(Long id) {
        return findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    
    protected void beforeSave(User user) {
        // Método base para ser sobreescrito
    }
} 