package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.stereotype.Component;

/**
 * Migración para crear la tabla de detalles de salida de productos.
 * 
 * ESTRUCTURA:
 * ----------
 * Detalles de cada salida de productos:
 * - product_output_id: ID de la salida principal
 * - product_id: Producto que sale
 * - quantity: Cantidad
 * - unit_price: Precio unitario de salida
 * 
 * EJEMPLOS:
 * --------
 * Para una venta de bidones:
 * 1. Cabecera (product_outputs):
 *    - employee_id: 1
 *    - order_id: 123
 *    - description: "Venta de bidones de agua"
 * 
 * 2. Detalle (product_output_details):
 *    - product_output_id: ID de la salida
 *    - product_id: ID del bidón
 *    - quantity: 5
 *    - unit_price: 15.00
 */
@Component
public class V14__CreateProductOutputDetailsTable implements JavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("""
                CREATE TABLE product_output_details (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    product_output_id BIGINT NOT NULL,
                    product_id BIGINT NOT NULL,
                    quantity INT NOT NULL,
                    unit_price DECIMAL(10,2) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    INDEX idx_product_output (product_output_id),
                    INDEX idx_product (product_id),
                    CONSTRAINT fk_output_details_output
                        FOREIGN KEY (product_output_id) REFERENCES product_outputs(id),
                    CONSTRAINT fk_output_details_product
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
        return MigrationVersion.fromVersion("14");
    }

    @Override
    public String getDescription() { 
        return "Create product output details table";
    }
}
