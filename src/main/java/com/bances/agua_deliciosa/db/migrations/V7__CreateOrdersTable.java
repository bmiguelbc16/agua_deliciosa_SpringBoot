package com.bances.agua_deliciosa.db.migrations;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

@Component
public class V7__CreateOrdersTable implements Migration {
    @Override
    public void migrate(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS orders (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    client_id BIGINT NOT NULL,
                    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                    delivery_address TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    FOREIGN KEY fk_order_client (client_id) 
                        REFERENCES clients(id) ON DELETE CASCADE,
                    INDEX idx_orders_client (client_id),
                    INDEX idx_orders_date (registration_date)
                )
            """);
        }
    }
} 