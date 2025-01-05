package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.stereotype.Component;

/**
 * Migración para crear la tabla de salidas de productos.
 * 
 * La tabla product_outputs almacena:
 * - Campos principales: user_id, date, status
 * - Campo opcional: notes
 * - Timestamps automáticos
 * 
 * Características:
 * - Índice en user_id
 * - Clave foránea a users
 * - Estado predeterminado 'pending'
 * - Fecha obligatoria
 * 
 * Se relaciona con:
 * - users: usuario que registra la salida
 * - product_output_details: productos retirados
 * Uso: Control de salidas de inventario
 */
@Component
public class V12__CreateProductOutputsTable implements JavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("""
                CREATE TABLE product_outputs (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    user_id BIGINT NOT NULL,
                    date DATE NOT NULL,
                    notes TEXT,
                    status VARCHAR(20) NOT NULL DEFAULT 'pending',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    INDEX idx_user_id (user_id),
                    CONSTRAINT fk_product_outputs_user_id FOREIGN KEY (user_id) REFERENCES users(id)
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
        return MigrationVersion.fromVersion("12");
    }

    @Override
    public String getDescription() { 
        return "Create product outputs table";
    }
}
