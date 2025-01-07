package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.stereotype.Component;

/**
 * Migración para crear la tabla de productos.
 * 
 * ESTRUCTURA:
 * ----------
 * PRODUCTOS (products):
 * - Campos básicos:
 *   * name: Nombre del producto
 *   * description: Descripción
 *   * sale_price: Precio de venta
 *   * for_sale: Si está activo para venta
 *   * stock: Cantidad actual
 * 
 * EJEMPLOS DE USO:
 * --------------
 * 1. Producto para venta (Bidón con agua):
 *    - name: "Bidón de agua purificada 20L"
 *    - for_sale: true
 *    - sale_price: 15.00
 *    - stock: 100
 * 
 * 2. Producto no vendible (Bidón vacío):
 *    - name: "Bidón vacío 20L"
 *    - for_sale: false
 *    - sale_price: 0
 *    - stock: 50
 */
@Component
public class V9__CreateProductsTable implements JavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            // Tabla de productos
            statement.execute("""
                CREATE TABLE products (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    name VARCHAR(255) NOT NULL,
                    description TEXT,
                    sale_price DECIMAL(10,2) NOT NULL DEFAULT 0,
                    for_sale BOOLEAN NOT NULL DEFAULT false,
                    stock INT NOT NULL DEFAULT 0,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    UNIQUE KEY unique_product_name (name),
                    CONSTRAINT chk_stock CHECK (stock >= 0),
                    CONSTRAINT chk_sale_price CHECK (
                        (for_sale = false AND sale_price = 0) OR
                        (for_sale = true AND sale_price > 0)
                    )
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
