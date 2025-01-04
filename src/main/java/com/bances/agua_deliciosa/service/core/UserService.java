package com.bances.agua_deliciosa.service.core;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bances.agua_deliciosa.dto.admin.ProfileDTO;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.UserRepository;
import com.bances.agua_deliciosa.model.Gender;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public ProfileDTO getCurrentUserProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return toProfileDTO(user);
    }
    
    @Transactional
    public void updateProfile(ProfileDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Validar email único si ha cambiado
        if (!user.getEmail().equals(dto.getEmail()) && 
            userRepository.existsByEmailAndIdNot(dto.getEmail(), user.getId())) {
            throw new RuntimeException("El email ya está registrado por otro usuario");
        }
        
        // Validar DNI único si ha cambiado
        if (!user.getDocumentNumber().equals(dto.getDocumentNumber()) && 
            userRepository.existsByDocumentNumberAndIdNot(dto.getDocumentNumber(), user.getId())) {
            throw new RuntimeException("El DNI ya está registrado por otro usuario");
        }
        
        updateUserFromProfileDTO(user, dto);
        
        // Solo actualizar la contraseña si se proporciona una nueva
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        
        userRepository.save(user);
    }
    
    private ProfileDTO toProfileDTO(User user) {
        ProfileDTO dto = new ProfileDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setDocumentNumber(user.getDocumentNumber());
        dto.setGender(user.getGender().name());
        dto.setBirthDate(user.getBirthDate());
        return dto;
    }
    
    private void updateUserFromProfileDTO(User user, ProfileDTO dto) {
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setDocumentNumber(dto.getDocumentNumber());
        user.setGender(Gender.valueOf(dto.getGender()));
        user.setBirthDate(dto.getBirthDate());
    }
}
