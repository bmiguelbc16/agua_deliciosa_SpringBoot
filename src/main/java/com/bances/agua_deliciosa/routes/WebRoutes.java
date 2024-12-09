package com.bances.agua_deliciosa.routes;

import org.springframework.context.annotation.Configuration;

@Configuration
public class WebRoutes {
    public static final String[] PUBLIC_PATHS = {
        // Rutas base
        "/",
        "/dist/**", 
        "/assets/**", 
        "/plugins/**",
        
        // Rutas de autenticación (equivalente a Auth::routes())
        "/login",
        "/logout",
        "/password/reset",
        "/password/confirm",
        "/email/verify/**"
    };
    
    // Equivalentes a los names() de Laravel
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String HOME = "/";
} 