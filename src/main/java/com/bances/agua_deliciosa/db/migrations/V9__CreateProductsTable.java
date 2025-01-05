package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.stereotype.Component;

/**
 * Migración para crear la tabla de productos.
 * 
 * La tabla products almacena:
 * - Campos principales: name, description, price
 * - Control de inventario: stock
 * - Estado del producto: active
 * - Timestamps automáticos
 * 
 * Características:
 * - Clave única en name
 * - Precio con precisión decimal(10,2)
 * - Stock por defecto en 0
 * - Estado activo por defecto
 * 
 * Se relaciona con:
 * - order_details: productos en pedidos
 * - product_entries: entradas de inventario
 * - product_outputs: salidas de inventario
 */
@Component
public class V9__CreateProductsTable implements JavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("""
                CREATE TABLE products (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    name VARCHAR(255) NOT NULL,
                    description TEXT,
                    price DECIMAL(10,2) NOT NULL,
                    stock INT NOT NULL DEFAULT 0,
                    active BOOLEAN DEFAULT true,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    UNIQUE KEY unique_name (name)
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
        return MigrationVersion.fromVersion("9");
    }

    @Override
    public String getDescription() { 
        return "Create products table";
    }
}
