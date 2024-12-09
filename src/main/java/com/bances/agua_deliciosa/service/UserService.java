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
import java.time.LocalDateTime;
import java.util.UUID;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Set;

@Service
@Transactional
@Slf4j
public class UserService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Intentando autenticar usuario con email: {}", email);
        
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> {
                log.error("Usuario no encontrado con email: {}", email);
                return new UsernameNotFoundException("Usuario no encontrado con email: " + email);
            });
        
        log.debug("Usuario encontrado: {}", user.getEmail());
        log.debug("Password hash en DB: {}", user.getPassword());
        log.debug("Roles del usuario: {}", user.getRoles());
        
        Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> {
                String authority = role.getName();
                log.debug("Agregando rol: {}", authority);
                return new SimpleGrantedAuthority(authority);
            })
            .collect(Collectors.toSet());
        
        log.debug("Authorities finales: {}", authorities);
        
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            user.isActive(),
            true,
            true,
            true,
            authorities
        );
    }
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
    
    public void sendPasswordResetEmail(String email) {
        User user = findByEmail(email);
        String token = UUID.randomUUID().toString();
        user.setRememberToken(token);
        userRepository.save(user);
        emailService.sendPasswordResetEmail(email, token);
    }
    
    public void verifyEmail(Long id, String hash) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
        if (!hash.equals(generateVerificationHash(user))) {
            throw new RuntimeException("Hash inválido");
        }
        
        user.setEmailVerifiedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    
    private String generateVerificationHash(User user) {
        return DigestUtils.sha256Hex(user.getEmail() + user.getId());
    }
    
    public void resendVerificationEmail() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = findByEmail(email);
        String hash = generateVerificationHash(user);
        emailService.sendVerificationEmail(user.getEmail(), user.getId(), hash);
    }
    
    public boolean confirmPassword(String email, String password) {
        User user = findByEmail(email);
        return passwordEncoder.matches(password, user.getPassword());
    }
    
    public void sendResetLinkEmail(String email) {
        User user = findByEmail(email);
        String token = UUID.randomUUID().toString();
        user.setRememberToken(token);
        userRepository.save(user);
        emailService.sendPasswordResetEmail(email, token);
    }
    
    public boolean isValidResetToken(String token) {
        return userRepository.findByRememberToken(token).isPresent();
    }
    
    public void resetPassword(String token, String email, String password) {
        User user = userRepository.findByRememberToken(token)
            .orElseThrow(() -> new RuntimeException("Token inválido"));
        user.setPassword(passwordEncoder.encode(password));
        user.setRememberToken(null);
        userRepository.save(user);
    }
} 