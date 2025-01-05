package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.stereotype.Component;

/**
 * Migración para crear la tabla de detalles de pedidos.
 * 
 * La tabla order_details almacena:
 * - Campos principales: order_id, product_id, quantity
 * - Campos de precio: unit_price, subtotal
 * - Timestamps automáticos
 * 
 * Características:
 * - Índices en order_id y product_id
 * - Claves foráneas a orders y products
 * - Precios con precisión decimal(10,2)
 * - Cantidad obligatoria
 * 
 * Se relaciona con:
 * - orders: pedido al que pertenece
 * - products: producto incluido
 * Uso: Desglose de productos por pedido
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
                    subtotal DECIMAL(10,2) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    INDEX idx_order_id (order_id),
                    INDEX idx_product_id (product_id),
                    CONSTRAINT fk_order_details_order_id FOREIGN KEY (order_id) REFERENCES orders(id),
                    CONSTRAINT fk_order_details_product_id FOREIGN KEY (product_id) REFERENCES products(id)
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
