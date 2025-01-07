package com.bances.agua_deliciosa.service.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bances.agua_deliciosa.model.Client;
import com.bances.agua_deliciosa.model.Employee;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.ClientRepository;
import com.bances.agua_deliciosa.repository.EmployeeRepository;
import com.bances.agua_deliciosa.repository.UserRepository;
import com.bances.agua_deliciosa.security.UserDetailsAdapter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityService {
    
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository;
    private final AuthenticationManager authenticationManager;

    @Transactional(readOnly = true)
    public User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetailsAdapter) {
            return ((UserDetailsAdapter) principal).getUser();
        }

        if (principal instanceof String) {
            String email = (String) principal;
            return userRepository.findByEmail(email).orElse(null);
        }

        return null;
    }

    public Employee getEmployee() {
        User user = getUser();
        if (user != null && "Employee".equals(user.getUserableType())) {
            return employeeRepository.findById(user.getUserableId()).orElse(null);
        }
        return null;
    }

    public Client getClient() {
        User user = getUser();
        if (user != null && "Client".equals(user.getUserableType())) {
            return clientRepository.findById(user.getUserableId()).orElse(null);
        }
        return null;
    }

    public boolean isEmployee() {
        User user = getUser();
        return user != null && "Employee".equals(user.getUserableType());
    }

    public boolean isClient() {
        User user = getUser();
        return user != null && "Client".equals(user.getUserableType());
    }

    public boolean hasRole(String roleName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(ga -> ga.getAuthority().equals("ROLE_" + roleName));
    }

    public boolean hasAnyRole(String... roles) {
        for (String role : roles) {
            if (hasRole(role)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated();
    }

    public boolean isOwner(String userType, Long resourceId) {
        User currentUser = getUser();
        if (currentUser == null) {
            return false;
        }

        if ("Employee".equals(userType)) {
            return currentUser.getUserableType().equals(userType) && 
                    currentUser.getUserableId().equals(resourceId);
        }

        if ("Client".equals(userType)) {
            return currentUser.getUserableType().equals(userType) && 
                    currentUser.getUserableId().equals(resourceId);
        }

        return false;
    }

    public boolean isEmailVerified(String email) {
        return userRepository.findByEmail(email)
                .map(user -> isVerified(user))
                .orElse(false);
    }

    public boolean isVerified(User user) {
        return user.getVerifications().stream()
                .anyMatch(v -> v.isActive() && v.getVerifiedAt() != null);
    }

    public Authentication authenticate(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return authentication;
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            throw e;
        }
    }
}
