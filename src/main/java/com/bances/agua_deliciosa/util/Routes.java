package com.bances.agua_deliciosa.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Routes {
    
    public static final class Web {
        private Web() {}
        
        public static final String HOME = "/";
        public static final String LOGIN = "/auth/login";
        public static final String REGISTER = "/register";
        public static final String FORGOT_PASSWORD = "/password/forgot";
        public static final String RESET_PASSWORD = "/password/reset";
        public static final String VERIFY_EMAIL = "/email/verify";
        
        public static final String[] PUBLIC_ROUTES = {
            HOME,
            LOGIN,
            REGISTER,
            FORGOT_PASSWORD,
            RESET_PASSWORD,
            VERIFY_EMAIL,
            "/css/**",
            "/js/**",
            "/images/**",
            "/webjars/**"
        };
    }
    
    public static final class Admin {
        private Admin() {}
        
        public static final String PREFIX = "/admin";
        
        // Rutas principales
        public static final String DASHBOARD = PREFIX + "/dashboard";
        public static final String PROFILE = PREFIX + "/profile";
        
        // Empleados
        public static final String EMPLOYEES = PREFIX + "/employees";
        public static final String EMPLOYEES_CREATE = EMPLOYEES + "/create";
        public static final String EMPLOYEES_EDIT = EMPLOYEES + "/edit/{id}";
        public static final String EMPLOYEES_UPDATE = EMPLOYEES + "/update/{id}";
        public static final String EMPLOYEES_DELETE = EMPLOYEES + "/delete/{id}";
        public static final String EMPLOYEES_ALL = EMPLOYEES + "/**";
        
        // Clientes
        public static final String CLIENTS = PREFIX + "/clients";
        public static final String CLIENTS_CREATE = CLIENTS + "/create";
        public static final String CLIENTS_EDIT = CLIENTS + "/edit/{id}";
        public static final String CLIENTS_UPDATE = CLIENTS + "/update/{id}";
        public static final String CLIENTS_DELETE = CLIENTS + "/delete/{id}";
        public static final String CLIENTS_ALL = CLIENTS + "/**";
        
        // Roles y Permisos
        public static final String ROLES = PREFIX + "/roles";
        public static final String ROLES_CREATE = ROLES + "/create";
        public static final String ROLES_EDIT = ROLES + "/edit/{id}";
        public static final String ROLES_UPDATE = ROLES + "/update/{id}";
        public static final String ROLES_DELETE = ROLES + "/delete/{id}";
        
        public static final String PERMISSIONS = PREFIX + "/permissions";
        public static final String PERMISSIONS_CREATE = PERMISSIONS + "/create";
        public static final String PERMISSIONS_EDIT = PERMISSIONS + "/edit/{id}";
        public static final String PERMISSIONS_UPDATE = PERMISSIONS + "/update/{id}";
        public static final String PERMISSIONS_DELETE = PERMISSIONS + "/delete/{id}";
        
        // Productos
        public static final String PRODUCTS = PREFIX + "/products";
        public static final String PRODUCTS_CREATE = PRODUCTS + "/create";
        public static final String PRODUCTS_EDIT = PRODUCTS + "/edit/{id}";
        public static final String PRODUCTS_UPDATE = PRODUCTS + "/update/{id}";
        public static final String PRODUCTS_DELETE = PRODUCTS + "/delete/{id}";
        
        // Rutas protegidas para configuración de seguridad
        public static final String[] PROTECTED_ROUTES = {
            PREFIX + "/**"  // Todas las rutas admin requieren autenticación
        };
        
        // Rutas que requieren roles específicos
        public static final String[] ADMIN_ONLY_ROUTES = {
            ROLES + "/**",
            PERMISSIONS + "/**"
        };
        
        public static final String[] MANAGER_ROUTES = {
            EMPLOYEES + "/**",
            PRODUCTS + "/**"
        };
    }
    
    public static final class Client {
        private Client() {}
        
        public static final String PREFIX = "/client";
        
        // Rutas principales
        public static final String DASHBOARD = PREFIX + "/dashboard";
        public static final String PROFILE = PREFIX + "/profile";
        public static final String PROFILE_UPDATE = PROFILE + "/update";
        
        // Pedidos
        public static final String ORDERS = PREFIX + "/orders";
        public static final String ORDERS_CREATE = ORDERS + "/create";
        public static final String ORDERS_DETAIL = ORDERS + "/{id}";
        public static final String ORDERS_CANCEL = ORDERS + "/{id}/cancel";
        
        // Productos
        public static final String PRODUCTS = PREFIX + "/products";
        public static final String PRODUCTS_DETAIL = PRODUCTS + "/{id}";
        
        // Carrito
        public static final String CART = PREFIX + "/cart";
        public static final String CART_ADD = CART + "/add";
        public static final String CART_REMOVE = CART + "/remove";
        public static final String CART_UPDATE = CART + "/update";
        
        public static final String[] PROTECTED_ROUTES = {
            PREFIX + "/**"  // Todas las rutas de cliente requieren autenticación
        };
    }
    
    public static final class Api {
        private Api() {}
        
        public static final String PREFIX = "/api/v1";
        
        // Autenticación
        public static final String AUTH = PREFIX + "/auth";
        public static final String LOGIN = AUTH + "/login";
        public static final String REGISTER = AUTH + "/register";
        public static final String REFRESH_TOKEN = AUTH + "/refresh";
        
        // API Admin
        public static final String ADMIN = PREFIX + "/admin";
        public static final String ADMIN_EMPLOYEES = ADMIN + "/employees";
        public static final String ADMIN_CLIENTS = ADMIN + "/clients";
        public static final String ADMIN_ROLES = ADMIN + "/roles";
        public static final String ADMIN_PERMISSIONS = ADMIN + "/permissions";
        public static final String ADMIN_PRODUCTS = ADMIN + "/products";
        
        // API Cliente
        public static final String CLIENT = PREFIX + "/client";
        public static final String CLIENT_ORDERS = CLIENT + "/orders";
        public static final String CLIENT_PRODUCTS = CLIENT + "/products";
        public static final String CLIENT_CART = CLIENT + "/cart";
        
        public static final String[] PUBLIC_ROUTES = {
            LOGIN,
            REGISTER
        };
        
        public static final String[] PROTECTED_ROUTES = {
            PREFIX + "/**"  // Todas las rutas API excepto las públicas requieren autenticación
        };
    }
}