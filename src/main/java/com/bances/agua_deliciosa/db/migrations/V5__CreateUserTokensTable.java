package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.stereotype.Component;

/**
 * Migración para crear la tabla de tokens de usuario.
 * 
 * La tabla user_tokens almacena:
 * - Campos principales: user_id, token, expires_at
 * - Token único para cada registro
 * - Fecha de expiración opcional
 * - Timestamp de creación
 * 
 * Características:
 * - Clave única en token
 * - Índice en user_id
 * - Clave foránea a users con eliminación en cascada
 * - Sin timestamp de actualización
 * 
 * Se relaciona con:
 * - users: a través de user_id
 * Uso: Gestión de sesiones y autenticación API
 */
@Component
public class V5__CreateUserTokensTable implements JavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("""
                CREATE TABLE user_tokens (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    user_id BIGINT NOT NULL,
                    token VARCHAR(64) NOT NULL,
                    expires_at TIMESTAMP NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    UNIQUE KEY unique_token (token),
                    INDEX idx_user_id (user_id),
                    CONSTRAINT fk_user_tokens_user_id FOREIGN KEY (user_id) 
                        REFERENCES users(id) ON DELETE CASCADE
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
        return MigrationVersion.fromVersion("5");
    }

    @Override
    public String getDescription() { 
        return "Create user_tokens table if not exists";
    }
}
