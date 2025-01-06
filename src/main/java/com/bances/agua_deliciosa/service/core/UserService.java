package com.bances.agua_deliciosa.service.core;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bances.agua_deliciosa.dto.admin.ProfileDTO;
import com.bances.agua_deliciosa.model.Gender;
import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.ClientRepository;
import com.bances.agua_deliciosa.repository.EmployeeRepository;
import com.bances.agua_deliciosa.repository.RoleRepository;
import com.bances.agua_deliciosa.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Transactional
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null) {
            Role defaultRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Rol por defecto no encontrado"));
            user.setRole(defaultRole);
        }

        User savedUser = userRepository.save(user);
        emailService.sendWelcomeEmail(user.getEmail(), user.getName(), "verification-link");
        return savedUser;
    }

    public ProfileDTO getCurrentUserProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = findByEmail(email);
        return toProfileDTO(user);
    }
    
    @Transactional
    public void updateProfile(ProfileDTO profileDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = findByEmail(email);
        
        if (!user.getEmail().equals(profileDTO.getEmail()) && 
            userRepository.existsByEmailAndIdNot(profileDTO.getEmail(), user.getId())) {
            throw new RuntimeException("El email ya está registrado por otro usuario");
        }

        // Verificar contraseña actual si se está cambiando la contraseña
        if (profileDTO.getNewPassword() != null && !profileDTO.getNewPassword().isEmpty()) {
            if (!passwordEncoder.matches(profileDTO.getCurrentPassword(), user.getPassword())) {
                throw new RuntimeException("La contraseña actual es incorrecta");
            }
            if (!profileDTO.getNewPassword().equals(profileDTO.getConfirmPassword())) {
                throw new RuntimeException("Las contraseñas no coinciden");
            }
            user.setPassword(passwordEncoder.encode(profileDTO.getNewPassword()));
            emailService.sendPasswordChangedEmail(user.getEmail());
        }

        updateUserFromProfileDTO(user, profileDTO);
        userRepository.save(user);
    }
    
    private void updateUserFromProfileDTO(User user, ProfileDTO dto) {
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getGender() != null) {
            user.setGender(Gender.valueOf(dto.getGender()));
        }
        if (dto.getBirthDate() != null) {
            user.setBirthDate(dto.getBirthDate());
        }
    }
    
    private ProfileDTO toProfileDTO(User user) {
        ProfileDTO dto = new ProfileDTO();
        dto.setName(user.getName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        if (user.getGender() != null) {
            dto.setGender(user.getGender().name());
        }
        dto.setBirthDate(user.getBirthDate());
        return dto;
    }

    public Object getUserable(User user) {
        if ("Client".equals(user.getUserableType())) {
            return clientRepository.findById(user.getUserableId());
        } else if ("Employee".equals(user.getUserableType())) {
            return employeeRepository.findById(user.getUserableId());
        }
        return Optional.empty();
    }
}
