package com.bances.agua_deliciosa.security;

import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.service.auth.SecurityService;
import com.bances.agua_deliciosa.service.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserSecurity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private transient final SecurityService securityService;
    private transient final UserService userService;

    public boolean isOwner(Authentication authentication, Long userId) {
        return getCurrentUser(authentication)
                .map(user -> user.getId().equals(userId))
                .orElse(false);
    }

    public boolean hasRole(Authentication authentication, String role) {
        return getCurrentUser(authentication)
                .map(user -> securityService.hasRole(user.getId(), role))
                .orElse(false);
    }

    private Optional<User> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        String email = authentication.getName();
        return userService.findByEmail(email);
    }
}
