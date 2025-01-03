package com.bances.agua_deliciosa.service.core;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.UserRepository;
import com.bances.agua_deliciosa.service.auth.SecurityService;
import com.bances.agua_deliciosa.dto.admin.ProfileDTO;

@Service
@Transactional(readOnly = true)
public class ProfileService {
    
    private final UserRepository userRepository;
    private final SecurityService securityService;

    public ProfileService(UserRepository userRepository, SecurityService securityService) {
        this.userRepository = userRepository;
        this.securityService = securityService;
    }

    public ProfileDTO getCurrentUserProfile() {
        User currentUser = securityService.getCurrentUser();
        return convertToDTO(currentUser);
    }

    @Transactional
    public void updateProfile(ProfileDTO dto) {
        User user = securityService.getCurrentUser();
        // Actualizar campos del usuario con los datos del DTO
        userRepository.save(user);
    }

    private ProfileDTO convertToDTO(User user) {
        // Implementar conversión de User a ProfileDTO
        return new ProfileDTO();
    }
} 