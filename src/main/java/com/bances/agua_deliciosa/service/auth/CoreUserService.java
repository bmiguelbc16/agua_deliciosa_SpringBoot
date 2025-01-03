package com.bances.agua_deliciosa.service.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.repository.UserRepository;
import com.bances.agua_deliciosa.service.core.RoleService;
import com.bances.agua_deliciosa.service.system.EmailService;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class CoreUserService extends UserService {
    
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final EmailService emailService;
    
    public CoreUserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        RoleService roleService,
        EmailService emailService
    ) {
        super(userRepository);
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.emailService = emailService;
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
        user.setRole(role);
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
    public void verifyEmail(Long userId) {
        User user = getById(userId);
        user.setEmailVerifiedAt(LocalDateTime.now());
        save(user);
        emailService.sendEmailVerifiedNotification(user.getEmail());
    }
    
    @Transactional
    public void resetPassword(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setPassword(passwordEncoder.encode(password));
        save(user);
    }
    
    @Transactional
    public void sendResetPasswordLink(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        emailService.sendResetPasswordLink(user.getEmail());
    }
    
    @Transactional
    public User updateUser(Long userId, User updatedUser, String roleName) {
        User user = getById(userId);
        
        // Actualizar campos básicos
        user.setName(updatedUser.getName());
        user.setLastName(updatedUser.getLastName());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        user.setBirthDate(updatedUser.getBirthDate());
        user.setGender(updatedUser.getGender());
        
        // Actualizar email si ha cambiado
        if (!user.getEmail().equals(updatedUser.getEmail())) {
            if (userRepository.existsByEmailAndIdNot(updatedUser.getEmail(), userId)) {
                throw new RuntimeException("El email ya está registrado");
            }
            user.setEmail(updatedUser.getEmail());
        }
        
        // Actualizar documento si ha cambiado
        if (!user.getDocumentNumber().equals(updatedUser.getDocumentNumber())) {
            if (userRepository.existsByDocumentNumberAndIdNot(updatedUser.getDocumentNumber(), userId)) {
                throw new RuntimeException("El documento ya está registrado");
            }
            user.setDocumentNumber(updatedUser.getDocumentNumber());
        }
        
        // Actualizar rol si ha cambiado
        if (roleName != null && !user.getRole().getName().equals(roleName)) {
            Role role = roleService.getByName(roleName);
            user.setRole(role);
        }
        
        return save(user);
    }
}