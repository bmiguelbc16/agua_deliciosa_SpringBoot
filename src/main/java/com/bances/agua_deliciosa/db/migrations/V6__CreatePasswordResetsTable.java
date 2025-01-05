package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.stereotype.Component;

/**
 * Migración para crear la tabla de restablecimiento de contraseñas.
 * 
 * La tabla password_resets almacena:
 * - Campos principales: user_id, token, expires_at
 * - Token único para cada solicitud
 * - Fechas de creación y expiración
 * 
 * Características:
 * - Clave única en token
 * - Índice en user_id
 * - Clave foránea a users con eliminación en cascada
 * - Sin timestamp de actualización
 * 
 * Se relaciona con:
 * - users: a través de user_id
 * Uso: Gestión de restablecimiento de contraseñas
 */
@Component
public class V6__CreatePasswordResetsTable implements JavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("""
                CREATE TABLE password_resets (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    user_id BIGINT NOT NULL,
                    token VARCHAR(255) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    expires_at TIMESTAMP NULL,
                    PRIMARY KEY (id),
                    UNIQUE KEY unique_token (token),
                    INDEX idx_user_id (user_id),
                    CONSTRAINT fk_password_resets_user_id 
                        FOREIGN KEY (user_id) 
                        REFERENCES users(id) 
                        ON DELETE CASCADE
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
            """);
        }
    }

    @Override
    public boolean canExecuteInTransaction() { 
        return true; 
    }

    @Override
    public Integer getChecksum() { 
        return null; 
    }

    @Override
    public MigrationVersion getVersion() { 
        return MigrationVersion.fromVersion("6");
    }

    @Override
    public String getDescription() {
        return "Create password_resets table";
    }
}
