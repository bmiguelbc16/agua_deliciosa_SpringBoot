package com.bances.agua_deliciosa.routes;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiRoutes {
    public static final String[] PATHS = {
        "/api/users/**",
        "/api/products/**",
        "/api/orders/**",
        "/api/auth/**"
    };
    
    public static final String[] PUBLIC_PATHS = {
        "/api/auth/login",
        "/api/auth/register"
    };
    
    // Equivalentes a los names() de Laravel
    public static final String USER = "/api/user";
} 