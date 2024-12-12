package com.bances.agua_deliciosa.service.core;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bances.agua_deliciosa.dto.client.ClientProfileDTO;
import com.bances.agua_deliciosa.dto.admin.ProfileDTO;
import com.bances.agua_deliciosa.service.auth.SecurityService;
import com.bances.agua_deliciosa.service.auth.CoreUserService;
import com.bances.agua_deliciosa.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileService {
    
    private final SecurityService securityService;
    private final CoreUserService userService;
    private final ModelMapper mapper;
    
    public ClientProfileDTO getCurrentClientProfile() {
        User user = securityService.getCurrentUser();
        return mapper.map(user, ClientProfileDTO.class);
    }
    
    @Transactional
    public void updateClientProfile(ClientProfileDTO dto) {
        User user = securityService.getCurrentUser();
        updateUserFromDTO(user, dto);
        userService.save(user);
    }
    
    private void updateUserFromDTO(User user, ClientProfileDTO dto) {
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        
        if (dto.getNewPassword() != null && !dto.getNewPassword().isEmpty()) {
            if (!userService.checkPassword(user, dto.getCurrentPassword())) {
                throw new RuntimeException("La contraseña actual es incorrecta");
            }
            userService.updatePassword(user.getId(), dto.getNewPassword());
        }
    }
    
    public ProfileDTO getCurrentUserProfile() {
        User user = securityService.getCurrentUser();
        return mapper.map(user, ProfileDTO.class);
    }
    
    @Transactional
    public void updateProfile(ProfileDTO dto) {
        User user = securityService.getCurrentUser();
        updateAdminUserFromDTO(user, dto);
        userService.save(user);
    }
    
    private void updateAdminUserFromDTO(User user, ProfileDTO dto) {
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());
        
        if (dto.getNewPassword() != null && !dto.getNewPassword().isEmpty()) {
            if (!userService.checkPassword(user, dto.getCurrentPassword())) {
                throw new RuntimeException("La contraseña actual es incorrecta");
            }
            if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
                throw new RuntimeException("Las contraseñas no coinciden");
            }
            userService.updatePassword(user.getId(), dto.getNewPassword());
        }
    }
} 