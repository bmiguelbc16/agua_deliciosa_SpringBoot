package com.bances.agua_deliciosa.db.migrations;

import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

@Component
public class V1__CreatePermissionTables implements Migration {
    @Override
    public void migrate(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Permisos
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS permissions (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(125) NOT NULL,
                    guard_name VARCHAR(125) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    UNIQUE KEY idx_permissions_name_guard (name, guard_name)
                )
            """);

            // Roles
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS roles (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(125) NOT NULL,
                    guard_name VARCHAR(125) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    UNIQUE KEY idx_roles_name_guard (name, guard_name)
                )
            """);

            // Modelo-Permisos
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS model_has_permissions (
                    permission_id BIGINT NOT NULL,
                    model_id BIGINT NOT NULL,
                    model_type VARCHAR(255) NOT NULL DEFAULT 'com.bances.agua_deliciosa.model.User',
                    PRIMARY KEY (permission_id, model_id, model_type),
                    INDEX idx_model_permissions (model_id, model_type),
                    FOREIGN KEY fk_model_permission (permission_id) 
                        REFERENCES permissions (id) ON DELETE CASCADE
                )
            """);

            // Modelo-Roles
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS model_has_roles (
                    role_id BIGINT NOT NULL,
                    model_id BIGINT NOT NULL,
                    model_type VARCHAR(255) NOT NULL DEFAULT 'com.bances.agua_deliciosa.model.User',
                    PRIMARY KEY (role_id, model_id),
                    INDEX idx_model_roles (model_id, model_type),
                    FOREIGN KEY fk_model_role (role_id) 
                        REFERENCES roles (id) ON DELETE CASCADE
                )
            """);

            // Roles-Permisos
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS role_has_permissions (
                    permission_id BIGINT NOT NULL,
                    role_id BIGINT NOT NULL,
                    PRIMARY KEY (permission_id, role_id),
                    FOREIGN KEY fk_role_permission_p (permission_id) 
                        REFERENCES permissions (id) ON DELETE CASCADE,
                    FOREIGN KEY fk_role_permission_r (role_id) 
                        REFERENCES roles (id) ON DELETE CASCADE
                )
            """);
        }
    }
}