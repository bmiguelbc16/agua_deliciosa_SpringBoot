package com.bances.agua_deliciosa.service;

import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UserService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Intentando autenticar usuario: {}", username);
        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> {
                log.error("Usuario no encontrado: {}", username);
                return new UsernameNotFoundException("Usuario no encontrado: " + username);
            });
            
        log.debug("Usuario encontrado: {}", username);
        log.debug("Password hash en BD: {}", user.getPassword());
        log.debug("Roles del usuario: {}", user.getRoles());
            
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList())
        );
    }
    
    public User createUser(User user) {
        log.info("Creando nuevo usuario: {}", user.getUsername());
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }
    
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
} 