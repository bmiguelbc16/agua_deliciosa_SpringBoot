package com.bances.agua_deliciosa.routes;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminRoutes {
    public static final String[] PATHS = {
        // Dashboard y perfil
        "/admin/dashboard",
        "/admin/profile",
        
        // Seguridad
        "/admin/seguridad/trabajadores/**",
        
        // Registros
        "/admin/registros/clientes/**",
        "/admin/registros/pedidos/**",
        "/admin/registros/productos/**",
        "/admin/registros/ingreso-de-productos/**",
        "/admin/registros/salida-de-productos/**"
    };
    
    // Equivalentes a los names() de Laravel
    public static final String DASHBOARD = "/admin/dashboard";
    public static final String PROFILE = "/admin/profile";
    public static final String EMPLOYEES = "/admin/seguridad/trabajadores";
    public static final String CLIENTS = "/admin/registros/clientes";
    public static final String ORDERS = "/admin/registros/pedidos";
    public static final String PRODUCTS = "/admin/registros/productos";
    public static final String PRODUCT_ENTRIES = "/admin/registros/ingreso-de-productos";
    public static final String PRODUCT_OUTPUTS = "/admin/registros/salida-de-productos";
} 