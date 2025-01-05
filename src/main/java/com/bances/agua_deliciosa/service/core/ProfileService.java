package com.bances.agua_deliciosa.service.core;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bances.agua_deliciosa.dto.admin.ProfileDTO;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.UserRepository;
import com.bances.agua_deliciosa.service.auth.SecurityService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final SecurityService securityService;

    @Transactional(readOnly = true)
    public ProfileDTO getProfile() {
        User user = securityService.getUser();
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        ProfileDTO profile = new ProfileDTO();
        profile.setId(user.getId());
        profile.setName(user.getName());
        profile.setLastName(user.getLastName());
        profile.setEmail(user.getEmail());
        profile.setDocumentNumber(user.getDocumentNumber());
        profile.setBirthDate(user.getBirthDate());
        profile.setGender(user.getGender().name());
        profile.setPhoneNumber(user.getPhoneNumber());
        profile.setRoleName(user.getRole().getName());

        return profile;
    }

    @Transactional
    public ProfileDTO updateProfile(ProfileDTO dto) {
        User user = securityService.getUser();
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());

        userRepository.save(user);
        return getProfile();
    }
}