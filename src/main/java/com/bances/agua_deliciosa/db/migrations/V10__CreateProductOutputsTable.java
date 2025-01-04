package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.stereotype.Component;

@Component
public class V10__CreateProductOutputsTable extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("""
                CREATE TABLE product_outputs (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    employee_id BIGINT NOT NULL COMMENT 'Empleado que registra la salida',
                    output_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha y hora de la salida',
                    output_type VARCHAR(50) NOT NULL DEFAULT 'MANUAL' COMMENT 'Tipo: MANUAL, DAMAGE, LOSS, TRANSFER',
                    destination VARCHAR(255) COMMENT 'Destino de los productos (si aplica)',
                    reference_number VARCHAR(50) COMMENT 'Número de documento de referencia',
                    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT 'Monto total de la salida',
                    notes TEXT COMMENT 'Notas o comentarios',
                    is_canceled BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Indica si la salida fue cancelada',
                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    CONSTRAINT fk_product_outputs_employee FOREIGN KEY (employee_id) 
                        REFERENCES employees(id) ON DELETE RESTRICT,
                    CONSTRAINT chk_product_outputs_amount CHECK (total_amount >= 0),
                    INDEX idx_product_outputs_employee (employee_id),
                    INDEX idx_product_outputs_date (output_date),
                    INDEX idx_product_outputs_type (output_type),
                    INDEX idx_product_outputs_canceled (is_canceled)
                ) ENGINE=InnoDB COMMENT='Salidas de productos del inventario'
            """);
        }
    }
}
