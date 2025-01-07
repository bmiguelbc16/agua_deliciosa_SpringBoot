package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.stereotype.Component;

/**
 * Migración para crear la tabla de salidas de productos.
 * 
 * ESTRUCTURA:
 * ----------
 * Registro principal de salidas de productos:
 * - employee_id: Empleado que registra
 * - order_id: Pedido (si es venta)
 * - movement_type: Impacto monetario
 *   * POSITIVE: Ganancia (+dinero, ej: ventas)
 *   * NEGATIVE: Pérdida (-dinero, ej: mantenimiento)
 *   * NEUTRAL: Sin impacto (0, ej: elaboración)
 * - description: Descripción de la salida
 * 
 * EJEMPLOS:
 * --------
 * 1. Venta de bidones:
 *    - employee_id: 1
 *    - order_id: 123
 *    - description: "Venta de bidones de agua"
 *    - movement_type: "POSITIVE"
 * 
 * 2. Envío a mantenimiento:
 *    - employee_id: 1
 *    - order_id: null
 *    - description: "Envío de máquinas a mantenimiento"
 *    - movement_type: "NEGATIVE"
 * 
 * 3. Elaboración de agua:
 *    - employee_id: 1
 *    - order_id: null
 *    - description: "Bidones para llenado"
 *    - movement_type: "NEUTRAL"
 */
@Component
public class V12__CreateProductOutputsTable implements JavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("""
                CREATE TABLE product_outputs (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    employee_id BIGINT NOT NULL,
                    order_id BIGINT,
                    movement_type ENUM('NEGATIVE', 'NEUTRAL', 'POSITIVE') NOT NULL,
                    description TEXT NOT NULL,
                    total_amount DECIMAL(10,2) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    INDEX idx_employee (employee_id),
                    INDEX idx_order (order_id),
                    INDEX idx_movement_type (movement_type),
                    CONSTRAINT fk_outputs_employee
                        FOREIGN KEY (employee_id) REFERENCES employees(id),
                    CONSTRAINT fk_outputs_order
                        FOREIGN KEY (order_id) REFERENCES orders(id)
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
        return MigrationVersion.fromVersion("12");
    }

    @Override
    public String getDescription() { 
        return "Create product outputs table";
    }
}
