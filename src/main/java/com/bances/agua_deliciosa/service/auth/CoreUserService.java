package com.bances.agua_deliciosa.service.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.repository.UserRepository;
import com.bances.agua_deliciosa.service.core.RoleService;
import com.bances.agua_deliciosa.service.system.EmailService;
import com.bances.agua_deliciosa.service.system.TokenService;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class CoreUserService extends UserService {
    
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final EmailService emailService;
    private final TokenService tokenService;
    
    public CoreUserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        RoleService roleService,
        EmailService emailService,
        TokenService tokenService
    ) {
        super(userRepository);
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.emailService = emailService;
        this.tokenService = tokenService;
    }
    
    @Transactional
    public User save(User user) {
        beforeSave(user);
        return userRepository.save(user);
    }
    
    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
    
    @Transactional
    public User createUser(User user, String password, String roleName) {
        user.setPassword(passwordEncoder.encode(password));
        Role role = roleService.getByName(roleName);
        user.getRoles().add(role);
        return save(user);
    }
    
    @Transactional
    public void updatePassword(Long userId, String newPassword) {
        User user = getById(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        save(user);
    }
    
    @Override
    protected void beforeSave(User user) {
        super.beforeSave(user);
        if (user.getId() == null && userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }
    }
    
    @Transactional
    public void verifyEmail(Long userId, String hash) {
        User user = getById(userId);
        
        if (!tokenService.validateVerificationToken(user, hash)) {
            throw new RuntimeException("Hash de verificación inválido");
        }
        
        user.setEmailVerifiedAt(LocalDateTime.now());
        save(user);
        emailService.sendEmailVerifiedNotification(user.getEmail());
    }
    
    @Transactional
    public void resetPassword(String token, String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
        if (!tokenService.validateResetToken(user, token)) {
            throw new RuntimeException("Token de reseteo inválido");
        }
        
        user.setPassword(passwordEncoder.encode(password));
        user.setResetToken(null);
        user.setResetTokenCreatedAt(null);
        save(user);
    }
    
    @Transactional
    public void sendResetPasswordLink(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
        String token = tokenService.generatePasswordResetToken(user);
        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }
} 