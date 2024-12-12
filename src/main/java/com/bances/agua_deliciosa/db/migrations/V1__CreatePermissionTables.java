package com.bances.agua_deliciosa.db.migrations;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class V1__CreatePermissionTables implements Migration {
    
    @Override
    public void migrate(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Crear tabla permissions
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS permissions (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(125) NOT NULL,
                    description VARCHAR(255),
                    guard_name VARCHAR(125) NOT NULL DEFAULT 'web',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    UNIQUE KEY unique_permission (name, guard_name)
                )
            """);
            
            // Crear tabla roles
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS roles (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(125) NOT NULL,
                    description VARCHAR(255),
                    guard_name VARCHAR(125) NOT NULL DEFAULT 'web',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    UNIQUE KEY unique_role (name, guard_name)
                )
            """);
            
            // Tabla pivote role_has_permissions
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS role_has_permissions (
                    permission_id BIGINT NOT NULL,
                    role_id BIGINT NOT NULL,
                    PRIMARY KEY (permission_id, role_id),
                    CONSTRAINT fk_rhp_permission_id FOREIGN KEY (permission_id) 
                        REFERENCES permissions (id) ON DELETE CASCADE,
                    CONSTRAINT fk_rhp_role_id FOREIGN KEY (role_id) 
                        REFERENCES roles (id) ON DELETE CASCADE
                )
            """);
        }
    }
}