package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.stereotype.Component;

@Component
public class V7__CreateProductsTable extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("""
                CREATE TABLE products (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL COMMENT 'Nombre del producto',
                    description TEXT COMMENT 'Descripción detallada del producto',
                    category VARCHAR(50) NOT NULL DEFAULT 'WATER' COMMENT 'Categoría: WATER, DISPENSER, ACCESSORY',
                    price DECIMAL(10,2) NOT NULL COMMENT 'Precio de venta al público',
                    unit_price DECIMAL(10,2) NOT NULL COMMENT 'Precio de costo del producto',
                    stock INT NOT NULL DEFAULT 0 COMMENT 'Stock actual',
                    min_stock INT NOT NULL DEFAULT 5 COMMENT 'Stock mínimo para alertas',
                    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Estado: ACTIVE, INACTIVE, DISCONTINUED',
                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    CONSTRAINT chk_product_price CHECK (price >= unit_price),
                    CONSTRAINT chk_product_stock CHECK (stock >= 0),
                    INDEX idx_products_name (name),
                    INDEX idx_products_category (category),
                    INDEX idx_products_status (status)
                ) ENGINE=InnoDB COMMENT='Catálogo de productos'
            """);
        }
    }
}
