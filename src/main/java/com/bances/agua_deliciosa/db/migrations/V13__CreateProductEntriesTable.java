package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.stereotype.Component;

/**
 * Migración para crear la tabla de entradas de productos.
 * 
 * La tabla product_entries almacena:
 * - Campos principales: user_id, supplier_name, date, total
 * - Campos opcionales: invoice_number, notes
 * - Estado y timestamps automáticos
 * 
 * Características:
 * - Índice en user_id
 * - Clave foránea a users
 * - Estado predeterminado 'pending'
 * - Total con precisión decimal(10,2)
 * 
 * Se relaciona con:
 * - users: usuario que registra la entrada
 * - product_entry_details: productos ingresados
 * Uso: Control de entradas de inventario
 */
@Component
public class V13__CreateProductEntriesTable implements JavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("""
                CREATE TABLE product_entries (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    user_id BIGINT NOT NULL,
                    supplier_name VARCHAR(255) NOT NULL,
                    date DATE NOT NULL,
                    invoice_number VARCHAR(50),
                    total DECIMAL(10,2) NOT NULL,
                    notes TEXT,
                    status VARCHAR(20) NOT NULL DEFAULT 'pending',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    INDEX idx_user_id (user_id),
                    CONSTRAINT fk_product_entries_user_id FOREIGN KEY (user_id) REFERENCES users(id)
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
        return MigrationVersion.fromVersion("13");
    }

    @Override
    public String getDescription() { 
        return "Create product entries table";
    }
}
