package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.stereotype.Component;

/**
 * Migración para crear la tabla de verificaciones de usuario.
 * 
 * La tabla user_verifications almacena información sobre verificaciones y restablecimientos:
 * - Datos de verificación (token, verification_type, expiry_date)
 * - Estado de verificación (verified)
 * - Relación con usuario (user_id)
 * - Timestamps de auditoría (created_at, updated_at)
 * 
 * Características importantes:
 * - Clave foránea a users
 * - Índice para búsqueda rápida por token
 * - Timestamps automáticos
 * - Tipos de verificación: EMAIL_VERIFICATION, PASSWORD_RESET
 * 
 * Se relaciona con:
 * - users: usuario al que pertenece la verificación
 */
@Component
public class V7__CreateUserVerificationsTable implements JavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("""
                CREATE TABLE user_verifications (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    user_id BIGINT NOT NULL,
                    token VARCHAR(255) NOT NULL,
                    verification_type VARCHAR(20) NOT NULL,
                    expiry_date TIMESTAMP NOT NULL,
                    verified BOOLEAN NOT NULL DEFAULT FALSE,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    INDEX idx_token (token),
                    INDEX idx_user_type (user_id, verification_type),
                    CONSTRAINT fk_verifications_user_id FOREIGN KEY (user_id) REFERENCES users(id)
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
        return MigrationVersion.fromVersion("7");
    }

    @Override
    public String getDescription() { 
        return "Create user verifications table";
    }
}
