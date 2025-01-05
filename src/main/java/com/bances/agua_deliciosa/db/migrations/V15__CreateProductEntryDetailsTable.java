package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.stereotype.Component;

/**
 * Migración para crear la tabla de detalles de entradas de productos.
 * 
 * La tabla product_entry_details almacena:
 * - Campos principales: product_entry_id, product_id, quantity
 * - Campos de precio: unit_price, subtotal
 * - Timestamps automáticos
 * 
 * Características:
 * - Índices en product_entry_id y product_id
 * - Claves foráneas a product_entries y products
 * - Precios con precisión decimal(10,2)
 * - Cantidad obligatoria
 * 
 * Se relaciona con:
 * - product_entries: entrada a la que pertenece
 * - products: producto ingresado
 * Uso: Desglose de productos por entrada
 */
@Component
public class V15__CreateProductEntryDetailsTable implements JavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("""
                CREATE TABLE product_entry_details (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    product_entry_id BIGINT NOT NULL,
                    product_id BIGINT NOT NULL,
                    quantity INT NOT NULL,
                    unit_price DECIMAL(10,2) NOT NULL,
                    subtotal DECIMAL(10,2) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    INDEX idx_product_entry_id (product_entry_id),
                    INDEX idx_product_id (product_id),
                    CONSTRAINT fk_product_entry_details_entry_id FOREIGN KEY (product_entry_id) REFERENCES product_entries(id),
                    CONSTRAINT fk_product_entry_details_product_id FOREIGN KEY (product_id) REFERENCES products(id)
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
        return MigrationVersion.fromVersion("15");
    }

    @Override
    public String getDescription() { 
        return "Create product entry details table";
    }
}
