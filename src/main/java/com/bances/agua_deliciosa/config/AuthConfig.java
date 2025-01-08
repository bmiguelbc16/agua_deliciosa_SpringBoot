package com.bances.agua_deliciosa.config;

import com.bances.agua_deliciosa.repository.UserRepository;
import com.bances.agua_deliciosa.security.UserDetailsAdapter;
import com.bances.agua_deliciosa.service.auth.UserRoleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthConfig {
    
    @Bean
    public UserDetailsService userDetailsService(
            UserRepository userRepository, 
            UserRoleService userRoleService) {
        return username -> userRepository.findByEmail(username)
            .map(user -> new UserDetailsAdapter(user, userRoleService))
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService, 
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
