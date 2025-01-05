package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.stereotype.Component;

/**
 * Migración para crear la tabla de detalles de salidas de productos.
 * 
 * La tabla product_output_details almacena:
 * - Campos principales: product_output_id, product_id, quantity
 * - Campo opcional: notes
 * - Timestamps automáticos
 * 
 * Características:
 * - Índices en product_output_id y product_id
 * - Claves foráneas a product_outputs y products
 * - Cantidad obligatoria
 * - Notas opcionales en formato TEXT
 * 
 * Se relaciona con:
 * - product_outputs: salida a la que pertenece
 * - products: producto retirado
 * Uso: Desglose de productos por salida
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
                    notes TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    INDEX idx_product_output_id (product_output_id),
                    INDEX idx_product_id (product_id),
                    CONSTRAINT fk_product_output_details_output_id FOREIGN KEY (product_output_id) REFERENCES product_outputs(id),
                    CONSTRAINT fk_product_output_details_product_id FOREIGN KEY (product_id) REFERENCES products(id)
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
