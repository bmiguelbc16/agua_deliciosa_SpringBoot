package com.bances.agua_deliciosa.routes;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientRoutes {
    public static final String[] PATHS = {
        // Dashboard y perfil
        "/client/dashboard",
        "/client/profile",
        
        // Registros
        "/client/registros/pedidos/**"
    };
    
    // Equivalentes a los names() de Laravel
    public static final String DASHBOARD = "/client/dashboard";
    public static final String PROFILE = "/client/profile";
    public static final String ORDERS = "/client/registros/pedidos";
} 