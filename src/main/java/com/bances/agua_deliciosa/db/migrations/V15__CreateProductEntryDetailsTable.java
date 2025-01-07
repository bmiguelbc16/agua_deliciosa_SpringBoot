package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.stereotype.Component;

/**
 * Migración para crear la tabla de detalles de entrada de productos.
 * 
 * ESTRUCTURA:
 * ----------
 * Detalles de cada entrada de productos:
 * - product_entry_id: ID de la entrada principal
 * - product_id: Producto que ingresa
 * - quantity: Cantidad
 * - unit_price: Precio unitario de entrada
 * 
 * EJEMPLOS:
 * --------
 * Para una entrada de bidones:
 * 1. Cabecera (product_entries):
 *    - employee_id: 1
 *    - description: "Ingreso de bidones vacíos por devolución"
 * 
 * 2. Detalle (product_entry_details):
 *    - product_entry_id: ID de la entrada
 *    - product_id: ID del bidón
 *    - quantity: 5
 *    - unit_price: 10.00
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
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    INDEX idx_product_entry (product_entry_id),
                    INDEX idx_product (product_id),
                    CONSTRAINT fk_entry_details_entry
                        FOREIGN KEY (product_entry_id) REFERENCES product_entries(id),
                    CONSTRAINT fk_entry_details_product
                        FOREIGN KEY (product_id) REFERENCES products(id),
                    CONSTRAINT chk_quantity CHECK (quantity > 0)
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
