package com.bances.agua_deliciosa.service.auth;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bances.agua_deliciosa.model.Client;
import com.bances.agua_deliciosa.model.Employee;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.ClientRepository;
import com.bances.agua_deliciosa.repository.EmployeeRepository;
import com.bances.agua_deliciosa.repository.UserRepository;
import com.bances.agua_deliciosa.security.UserSecurity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SecurityService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    public Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Set<String> getCurrentUserRoles() {
        Authentication auth = getCurrentAuthentication();
        if (auth == null) return Set.of();

        return auth.getAuthorities().stream()
            .map(ga -> ga.getAuthority().replace("ROLE_", ""))
            .collect(Collectors.toSet());
    }

    public User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof UserSecurity) {
            String email = ((UserSecurity) principal).getUsername();
            return userRepository.findByEmail(email).orElse(null);
        }

        return null;
    }

    // Alias para mantener compatibilidad con código existente
    public User getCurrentUser() {
        return getUser();
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

    public boolean isAdmin() {
        User user = getUser();
        return user != null && user.getRole() != null && "Admin".equals(user.getRole().getName());
    }

    public boolean isEmployee() {
        User user = getUser();
        return user != null && "Employee".equals(user.getUserableType());
    }

    public boolean isClient() {
        User user = getUser();
        return user != null && "Client".equals(user.getUserableType());
    }

    public boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(ga -> ga.getAuthority().equals("ROLE_" + role.toUpperCase()));
    }

    public boolean hasAnyRole(String... roles) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;

        Set<String> userRoles = auth.getAuthorities().stream()
                .map(ga -> ga.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toSet());

        for (String role : roles) {
            if (userRoles.contains(role.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    public boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated();
    }

    public boolean hasAccess(String userType, Long resourceId) {
        User currentUser = getUser();
        if (currentUser == null) {
            return false;
        }

        if (isAdmin()) {
            return true;
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
                .map(User::getEmailVerifiedAt)
                .isPresent();
    }

    public Authentication authenticate(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return authentication;
        } catch (AuthenticationException e) {
            throw new RuntimeException("Error de autenticación: " + e.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserSecurity(user);
    }
}
