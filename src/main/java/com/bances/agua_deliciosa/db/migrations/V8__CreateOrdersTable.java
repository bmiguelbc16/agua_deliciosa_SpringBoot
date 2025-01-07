package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.stereotype.Component;

/**
 * Migración para crear las tablas relacionadas con pedidos.
 * 
 * ESTRUCTURA POLIMÓRFICA:
 * ---------------------
 * 1. PEDIDOS (orders):
 *    - Tabla base con campos comunes
 *    - Usa polimorfismo para store_orders y web_orders
 * 
 * 2. PEDIDOS TIENDA (store_orders):
 *    - Pedidos realizados en tienda
 *    - seller_employee_id: Empleado que toma el pedido
 *    - delivery_employee_id: Empleado que entrega
 * 
 * 3. PEDIDOS WEB (web_orders):
 *    - Pedidos realizados por cliente web
 *    - delivery_employee_id: Empleado que entrega
 */
@Component
public class V8__CreateOrdersTable implements JavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            // Tabla base de pedidos
            statement.execute("""
                CREATE TABLE orders (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    customer_id BIGINT NOT NULL,
                    orderable_type VARCHAR(50) NOT NULL,
                    orderable_id BIGINT NOT NULL,
                    delivery_date DATE NOT NULL,
                    delivery_address TEXT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    INDEX idx_customer (customer_id),
                    INDEX idx_orderable (orderable_type, orderable_id),
                    INDEX idx_delivery_date (delivery_date),
                    CONSTRAINT fk_orders_customer 
                        FOREIGN KEY (customer_id) REFERENCES customers(id)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
            """);

            // Tabla para pedidos en tienda (2 empleados: vendedor y repartidor)
            statement.execute("""
                CREATE TABLE store_orders (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    seller_employee_id BIGINT NOT NULL,
                    delivery_employee_id BIGINT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    INDEX idx_seller (seller_employee_id),
                    INDEX idx_delivery (delivery_employee_id),
                    CONSTRAINT fk_store_orders_seller
                        FOREIGN KEY (seller_employee_id) REFERENCES employees(id),
                    CONSTRAINT fk_store_orders_delivery
                        FOREIGN KEY (delivery_employee_id) REFERENCES employees(id)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
            """);

            // Tabla para pedidos web (solo empleado repartidor)
            statement.execute("""
                CREATE TABLE web_orders (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    delivery_employee_id BIGINT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    INDEX idx_delivery (delivery_employee_id),
                    CONSTRAINT fk_web_orders_delivery
                        FOREIGN KEY (delivery_employee_id) REFERENCES employees(id)
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
        return "Create orders tables";
    }
}
