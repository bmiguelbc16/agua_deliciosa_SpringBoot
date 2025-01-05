package com.bances.agua_deliciosa.db.migrations;

import org.springframework.stereotype.Component;
import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.MigrationVersion;

/**
 * Migración para crear la tabla base de clientes.
 * 
 * La tabla clients es una tabla base que almacena:
 * - ID único del cliente (id)
 * - Fechas de creación y actualización (created_at, updated_at)
 * 
 * Características:
 * - Tabla base para el polimorfismo con users
 * - Índice en id para optimizar búsquedas
 * - Timestamps automáticos
 * 
 * Se relaciona con:
 * - users: a través del polimorfismo (userable_type = 'App\\Models\\Client')
 * - orders: pedidos realizados por el cliente
 * Nota: Los datos personales y de contacto se almacenan en la tabla users
 */
@Component
public class V3__CreateClientsTable implements JavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("""
                CREATE TABLE clients (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    INDEX idx_id (id)
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
        return MigrationVersion.fromVersion("3");
    }

    @Override
    public String getDescription() { 
        return "Create clients table";
    }
}
