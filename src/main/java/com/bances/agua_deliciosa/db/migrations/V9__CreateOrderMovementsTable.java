package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.stereotype.Component;

@Component
public class V9__CreateOrderMovementsTable extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("""
                CREATE TABLE order_movements (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    order_id BIGINT NOT NULL COMMENT 'ID del pedido',
                    employee_id BIGINT NOT NULL COMMENT 'ID del empleado que realiza el movimiento',
                    movement_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha y hora del movimiento',
                    movement_type VARCHAR(50) NOT NULL COMMENT 'Tipo: ASSIGNED, PICKED_UP, IN_TRANSIT, DELIVERED, CANCELLED',
                    previous_status VARCHAR(50) COMMENT 'Estado anterior del pedido',
                    new_status VARCHAR(50) NOT NULL COMMENT 'Nuevo estado del pedido',
                    notes TEXT COMMENT 'Notas o comentarios del movimiento',
                    gps_location VARCHAR(100) COMMENT 'Ubicación GPS del movimiento',
                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    CONSTRAINT fk_order_movements_order FOREIGN KEY (order_id) 
                        REFERENCES orders(id) ON DELETE CASCADE,
                    CONSTRAINT fk_order_movements_employee FOREIGN KEY (employee_id) 
                        REFERENCES employees(id) ON DELETE RESTRICT,
                    INDEX idx_order_movements_order (order_id),
                    INDEX idx_order_movements_employee (employee_id),
                    INDEX idx_order_movements_date (movement_date),
                    INDEX idx_order_movements_type (movement_type)
                ) ENGINE=InnoDB COMMENT='Movimientos y cambios de estado de los pedidos'
            """);
        }
    }
}
