package com.bances.agua_deliciosa.db.migrations;

import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

@Component
public class V13__CreateProductOutputDetailsTable implements Migration {
    @Override
    public void migrate(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS product_output_details (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    product_output_id BIGINT NOT NULL,
                    product_id BIGINT NOT NULL,
                    quantity INT NOT NULL DEFAULT 0,
                    unit_price DECIMAL(8,2) NOT NULL DEFAULT 0.00,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    FOREIGN KEY fk_output_detail_output (product_output_id) 
                        REFERENCES product_outputs(id) ON DELETE CASCADE,
                    FOREIGN KEY fk_output_detail_product (product_id) 
                        REFERENCES products(id) ON DELETE RESTRICT,
                    INDEX idx_output_details_output (product_output_id),
                    INDEX idx_output_details_product (product_id)
                )
            """);
        }
    }
} 