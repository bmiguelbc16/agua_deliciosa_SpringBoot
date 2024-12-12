package com.bances.agua_deliciosa.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Routes {
    
    public static final class Web {
        public static final String HOME = "/";
        public static final String LOGIN = "/auth/login";
        public static final String REGISTER = "/register";
        public static final String FORGOT_PASSWORD = "/password/forgot";
        public static final String RESET_PASSWORD = "/password/reset";
        public static final String VERIFY_EMAIL = "/email/verify";
    }
    
    public static final class Admin {
        public static final String PREFIX = "/admin";
        public static final String DASHBOARD = PREFIX + "/dashboard";
        public static final String EMPLOYEES = PREFIX + "/employees";
        public static final String EMPLOYEES_ALL = EMPLOYEES + "/**";  // Para seguridad
        public static final String CLIENTS = PREFIX + "/clients";
        public static final String CLIENTS_ALL = CLIENTS + "/**";      // Para seguridad
        public static final String ROLES = PREFIX + "/roles";
        public static final String PRODUCTS = PREFIX + "/products";
        
        // Array de rutas protegidas para configuración de seguridad
        public static final String[] PROTECTED_ROUTES = {
            EMPLOYEES_ALL,
            CLIENTS_ALL,
            PREFIX + "/**"  // Cualquier otra ruta admin
        };
    }
    
    public static final class Client {
        public static final String PREFIX = "/client";
        public static final String DASHBOARD = PREFIX + "/dashboard";
        public static final String PROFILE = PREFIX + "/profile";
        public static final String ORDERS = PREFIX + "/orders";
        
        public static final String[] PROTECTED_ROUTES = {
            PREFIX + "/**"  // Todas las rutas de cliente
        };
    }
    
    public static final class Api {
        public static final String PREFIX = "/api";
        public static final String AUTH = PREFIX + "/auth";
        public static final String USERS = PREFIX + "/users";
        public static final String PRODUCTS = PREFIX + "/products";
        
        public static final String[] PUBLIC_ROUTES = {
            AUTH + "/login",
            AUTH + "/register"
        };
        
        public static final String[] PROTECTED_ROUTES = {
            PREFIX + "/**"  // Todas las rutas API excepto las públicas
        };
    }
    
    // Rutas públicas generales
    public static final String[] PUBLIC_ROUTES = {
        Web.LOGIN,
        Web.REGISTER,
        Web.FORGOT_PASSWORD,
        Web.RESET_PASSWORD,
        Web.VERIFY_EMAIL,
        "/css/**",
        "/js/**",
        "/images/**",
        "/auth/login"
    };
} 