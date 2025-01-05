package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.stereotype.Component;

/**
 * Migración para crear la tabla de usuarios del sistema.
 * 
 * La tabla users almacena toda la información de usuarios:
 * - Datos personales (name, last_name, birth_date, gender)
 * - Datos de contacto (email, phone_number)
 * - Datos de identificación (document_number)
 * - Datos de acceso (password, email_verified_at, remember_token)
 * - Datos de control (role_id, active)
 * - Datos polimórficos (userable_type, userable_id)
 * 
 * Características importantes:
 * - Claves únicas para email y document_number
 * - Índice para la relación polimórfica
 * - Clave foránea a roles
 * - Timestamps automáticos
 * 
 * Se relaciona con:
 * - roles: asignación de rol al usuario
 * - employees: cuando userable_type es 'App\\Models\\Employee'
 * - clients: cuando userable_type es 'App\\Models\\Client'
 */
@Component
public class V4__CreateUsersTable implements JavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("""
                CREATE TABLE users (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    name VARCHAR(255) NOT NULL,
                    last_name VARCHAR(255) NOT NULL,
                    email VARCHAR(255) NOT NULL,
                    document_number VARCHAR(20) NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    birth_date DATE,
                    gender VARCHAR(20),
                    phone_number VARCHAR(20),
                    role_id BIGINT,
                    userable_type VARCHAR(50) NOT NULL,
                    userable_id BIGINT NOT NULL,
                    email_verified_at TIMESTAMP NULL,
                    remember_token VARCHAR(100),
                    active BOOLEAN DEFAULT true,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    UNIQUE KEY unique_email (email),
                    UNIQUE KEY unique_document (document_number),
                    UNIQUE KEY unique_userable (userable_type, userable_id),
                    INDEX idx_userable (userable_type, userable_id),
                    CONSTRAINT fk_users_role_id FOREIGN KEY (role_id) REFERENCES roles(id)
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
        return MigrationVersion.fromVersion("4");
    }

    @Override
    public String getDescription() { 
        return "Create users table";
    }
}
