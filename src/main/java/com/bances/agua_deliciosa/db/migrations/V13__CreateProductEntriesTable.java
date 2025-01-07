package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.stereotype.Component;

/**
 * Migración para crear la tabla de entradas de productos.
 * 
 * ESTRUCTURA:
 * ----------
 * Registro principal de entradas de productos:
 * - employee_id: Empleado que registra
 * - description: Descripción del ingreso
 * - movement_type: Impacto monetario
 *   * NEGATIVE: Pérdida (-dinero, ej: compras)
 *   * NEUTRAL: Sin impacto (0, ej: devoluciones)
 *   * POSITIVE: Ganancia (+dinero, casos especiales)
 * 
 * EJEMPLOS:
 * --------
 * 1. Compra de bidones nuevos:
 *    - employee_id: 1
 *    - description: "Compra de bidones nuevos"
 *    - movement_type: "NEGATIVE"
 * 
 * 2. Devolución de bidones:
 *    - employee_id: 1
 *    - description: "Devolución de bidones vacíos"
 *    - movement_type: "NEUTRAL"
 */
@Component
public class V13__CreateProductEntriesTable implements JavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("""
                CREATE TABLE product_entries (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    employee_id BIGINT NOT NULL,
                    movement_type ENUM('NEGATIVE', 'NEUTRAL', 'POSITIVE') NOT NULL,
                    description TEXT NOT NULL,
                    total_amount DECIMAL(10,2) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    INDEX idx_employee (employee_id),
                    INDEX idx_movement_type (movement_type),
                    CONSTRAINT fk_entries_employee
                        FOREIGN KEY (employee_id) REFERENCES employees(id)
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
        return MigrationVersion.fromVersion("13");
    }

    @Override
    public String getDescription() { 
        return "Create product entries table";
    }
}
