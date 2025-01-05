package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.stereotype.Component;

/**
 * Migración para crear la tabla de pedidos.
 * 
 * La tabla orders almacena:
 * - Campos principales: employee_id (empleado que atiende), client_id (cliente que ordena), total
 * - Estados: status, payment_status
 * - Campos opcionales: payment_type, notes
 * - Timestamps automáticos
 * 
 * Características:
 * - Índices en employee_id y client_id
 * - Claves foráneas a employees y clients
 * - Estados predeterminados 'pending'
 * - Total con precisión decimal(10,2)
 * 
 * Se relaciona con:
 * - employees: empleado que registra/atiende el pedido
 * - clients: cliente que realiza el pedido
 * - order_details: productos del pedido
 * - order_movements: historial de cambios
 */
@Component
public class V8__CreateOrdersTable implements JavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("""
                CREATE TABLE orders (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    order_number VARCHAR(50) NOT NULL,
                    employee_id BIGINT,
                    client_id BIGINT NOT NULL,
                    total_amount DECIMAL(10,2) NOT NULL,
                    payment_method VARCHAR(50),
                    delivery_address TEXT,
                    delivery_date TIMESTAMP,
                    status VARCHAR(20) NOT NULL DEFAULT 'pending',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    UNIQUE KEY unique_order_number (order_number),
                    INDEX idx_employee_id (employee_id),
                    INDEX idx_client_id (client_id),
                    CONSTRAINT fk_orders_employee_id FOREIGN KEY (employee_id) REFERENCES employees(id),
                    CONSTRAINT fk_orders_client_id FOREIGN KEY (client_id) REFERENCES clients(id)
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
        return MigrationVersion.fromVersion("8");
    }

    @Override
    public String getDescription() { 
        return "Create orders table";
    }
}
