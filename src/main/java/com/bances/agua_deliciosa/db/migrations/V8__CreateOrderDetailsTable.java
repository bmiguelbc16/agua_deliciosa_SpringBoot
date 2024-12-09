package com.bances.agua_deliciosa.db.migrations;

import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

@Component
public class V8__CreateOrderDetailsTable implements Migration {
    @Override
    public void migrate(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS order_details (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    order_id BIGINT NOT NULL,
                    product_id BIGINT NOT NULL,
                    quantity INT NOT NULL DEFAULT 0,
                    unit_price DECIMAL(8,2) NOT NULL DEFAULT 0.00,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    FOREIGN KEY fk_detail_order (order_id) 
                        REFERENCES orders(id) ON DELETE CASCADE,
                    FOREIGN KEY fk_detail_product (product_id) 
                        REFERENCES products(id) ON DELETE CASCADE,
                    INDEX idx_details_order (order_id),
                    INDEX idx_details_product (product_id)
                )
            """);
        }
    }
} 