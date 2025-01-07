package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.stereotype.Component;

/**
 * Migración para crear las tablas de movimientos de pedidos.
 * 
 * ESTRUCTURA:
 * ----------
 * 1. order_movements:
 *    - Tabla maestra de movimientos posibles
 *    - Ejemplo: "En preparación", "En camino", etc.
 * 
 * 2. order_movement_details:
 *    - Tabla relacional entre orders y movements
 *    - Registra qué movimientos ha tenido cada orden
 */
@Component
public class V11__CreateOrderMovementsTable implements JavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            // Tabla maestra de movimientos
            statement.execute("""
                CREATE TABLE order_movements (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    title VARCHAR(100) NOT NULL,
                    description TEXT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    UNIQUE KEY unique_title (title)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
            """);

            // Tabla relacional de órdenes y movimientos
            statement.execute("""
                CREATE TABLE order_movement_details (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    order_id BIGINT NOT NULL,
                    movement_id BIGINT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    INDEX idx_order (order_id),
                    INDEX idx_movement (movement_id),
                    CONSTRAINT fk_movement_details_order 
                        FOREIGN KEY (order_id) REFERENCES orders(id),
                    CONSTRAINT fk_movement_details_movement 
                        FOREIGN KEY (movement_id) REFERENCES order_movements(id)
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
        return MigrationVersion.fromVersion("11");
    }

    @Override
    public String getDescription() { 
        return "Create order movements tables";
    }
}
