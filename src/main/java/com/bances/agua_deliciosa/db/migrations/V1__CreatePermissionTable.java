package com.bances.agua_deliciosa.db.migrations;

import org.springframework.stereotype.Component;
import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.MigrationVersion;
import java.sql.Statement;

/**
 * Migración para crear las tablas del sistema de permisos.
 * 
 * 1. Tabla permissions:
 * - Campos principales: name, description, guard_name, active
 * - Claves únicas: (name, guard_name)
 * - Índices: guard_name
 * - Timestamps automáticos
 * 
 * 2. Tabla roles:
 * - Campos principales: name, description, guard_name
 * - Claves únicas: (name, guard_name)
 * - Índices: guard_name
 * - Timestamps automáticos
 * 
 * 3. Tabla role_has_permissions:
 * - Tabla pivote many-to-many
 * - Claves foráneas: permission_id, role_id
 * - Índices en ambas claves foráneas
 * - Eliminación en cascada
 * 
 * Se relaciona con:
 * - users: a través de role_id
 * Implementa: Control de Acceso Basado en Roles (RBAC)
 */
@Component
public class V1__CreatePermissionTable implements JavaMigration {
    
    @Override
    public void migrate(Context context) throws Exception {
        try (Statement statement = context.getConnection().createStatement()) {
            // Crear tabla permissions
            statement.execute(
                """
                CREATE TABLE permissions (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(125) NOT NULL,
                    description VARCHAR(255),
                    guard_name VARCHAR(125) NOT NULL DEFAULT 'web',
                    active BOOLEAN NOT NULL DEFAULT true,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    UNIQUE KEY unique_permission (name, guard_name),
                    INDEX idx_guard_name (guard_name)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
            """);
            
            // Crear tabla roles
            statement.execute (
                """
                CREATE TABLE roles (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(125) NOT NULL,
                    description VARCHAR(255),
                    guard_name VARCHAR(125) NOT NULL DEFAULT 'web',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    UNIQUE KEY unique_role (name, guard_name),
                    INDEX idx_guard_name (guard_name)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
            """);
            
            // Tabla pivote role_has_permissions
            statement.execute(
                """
                CREATE TABLE role_has_permissions (
                    permission_id BIGINT NOT NULL,
                    role_id BIGINT NOT NULL,
                    PRIMARY KEY (permission_id, role_id),
                    CONSTRAINT fk_rhp_permission_id FOREIGN KEY (permission_id) 
                        REFERENCES permissions (id) ON DELETE CASCADE,
                    CONSTRAINT fk_rhp_role_id FOREIGN KEY (role_id) 
                        REFERENCES roles (id) ON DELETE CASCADE,
                    INDEX idx_permission_id (permission_id),
                    INDEX idx_role_id (role_id)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
            """);
        }
    }

    @Override
    public boolean canExecuteInTransaction() { return true; }

    @Override
    public Integer getChecksum() { return null; }

    @Override
    public MigrationVersion getVersion() { 
        return MigrationVersion.fromVersion("1");
    }

    @Override
    public String getDescription() { 
        return "Create permissions, roles and role_has_permissions tables";
    }
}
