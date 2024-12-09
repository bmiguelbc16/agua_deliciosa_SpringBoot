package com.bances.agua_deliciosa.config.provider;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.context.annotation.Bean;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableMethodSecurity
@Slf4j
public class AuthServiceProvider {
    
    @Bean
    public void initializeAuth() {
        log.info("Registrando políticas de autorización...");
        // Configuración de políticas de autorización
    }
}