package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.stereotype.Component;

/**
 * Migración para crear la tabla de detalles de pedido.
 * 
 * ESTRUCTURA:
 * ----------
 * Detalles de cada pedido:
 * - order_id: ID del pedido
 * - product_id: Producto pedido
 * - quantity: Cantidad
 * - unit_price: Precio unitario
 * 
 * EJEMPLOS:
 * --------
 * Para un pedido de bidones:
 * 1. Cabecera (orders):
 *    - customer_id: 1
 *    - status: "PENDING"
 *    - notes: "Entregar en la tarde"
 * 
 * 2. Detalle (order_details):
 *    - order_id: ID del pedido
 *    - product_id: ID del bidón
 *    - quantity: 5
 *    - unit_price: 15.00
 */
@Component
public class V10__CreateOrderDetailsTable implements JavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("""
                CREATE TABLE order_details (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    order_id BIGINT NOT NULL,
                    product_id BIGINT NOT NULL,
                    quantity INT NOT NULL,
                    unit_price DECIMAL(10,2) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    INDEX idx_order (order_id),
                    INDEX idx_product (product_id),
                    CONSTRAINT fk_order_details_order
                        FOREIGN KEY (order_id) REFERENCES orders(id),
                    CONSTRAINT fk_order_details_product
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
        return MigrationVersion.fromVersion("10");
    }

    @Override
    public String getDescription() { 
        return "Create order details table";
    }
}
