package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.stereotype.Component;

@Component
public class V6__CreateOrdersTable extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("""
                CREATE TABLE orders (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    client_id BIGINT NOT NULL COMMENT 'ID del cliente que realiza el pedido',
                    registration_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de registro del pedido',
                    status VARCHAR(50) NOT NULL DEFAULT 'PENDING' COMMENT 'Estado del pedido: PENDING, PROCESSING, DELIVERED, CANCELLED',
                    total DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT 'Monto total del pedido',
                    delivery_address TEXT NOT NULL COMMENT 'Dirección de entrega',
                    notes TEXT COMMENT 'Notas adicionales del pedido',
                    employee_id BIGINT COMMENT 'Empleado asignado al pedido',
                    payment_method VARCHAR(50) DEFAULT 'CASH' COMMENT 'Método de pago: CASH, TRANSFER, YAPE, PLIN',
                    payment_status VARCHAR(50) DEFAULT 'PENDING' COMMENT 'Estado del pago: PENDING, PAID, PARTIAL',
                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    CONSTRAINT fk_order_client FOREIGN KEY (client_id) 
                        REFERENCES clients(id) ON DELETE RESTRICT,
                    CONSTRAINT fk_order_employee FOREIGN KEY (employee_id)
                        REFERENCES employees(id) ON DELETE SET NULL,
                    INDEX idx_orders_client (client_id),
                    INDEX idx_orders_employee (employee_id),
                    INDEX idx_orders_status (status),
                    INDEX idx_orders_payment (payment_status),
                    INDEX idx_orders_date (registration_date)
                ) ENGINE=InnoDB COMMENT='Tabla de pedidos de agua y productos'
            """);
        }
    }
}
