package com.bances.agua_deliciosa.config;

/**
 * Clase que centraliza todas las rutas administrativas de la aplicación.
 * Esta clase es final y tiene un constructor privado para evitar instanciación.
 */
public final class AdminRoutes {
    private AdminRoutes() {}
    
    // Prefijos de rutas
    public static final String PREFIX = "/admin";
    
    // Rutas base
    public static final String DASHBOARD = "/dashboard";
    public static final String EMPLOYEES = "/employees";
    public static final String CLIENTS = "/clients";
    public static final String PERMISSIONS = "/permissions";
    public static final String PROFILE = "/profile";
    
    // Rutas completas
    public static final String DASHBOARD_FULL = PREFIX + DASHBOARD;
    public static final String EMPLOYEES_FULL = PREFIX + EMPLOYEES;
    public static final String CLIENTS_FULL = PREFIX + CLIENTS;
    public static final String PERMISSIONS_FULL = PREFIX + PERMISSIONS;
    public static final String PROFILE_FULL = PREFIX + PROFILE;
    
    // Sub-rutas
    public static final String CREATE = "/create";
    public static final String EDIT = "/edit";
    public static final String UPDATE = "/update";
    public static final String DELETE = "/delete";
}
