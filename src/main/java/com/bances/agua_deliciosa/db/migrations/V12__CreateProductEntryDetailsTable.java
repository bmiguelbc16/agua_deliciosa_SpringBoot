package com.bances.agua_deliciosa.db.migrations;

import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

@Component
public class V12__CreateProductEntryDetailsTable implements Migration {
    @Override
    public void migrate(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS product_entry_details (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    product_entry_id BIGINT NOT NULL,
                    product_id BIGINT NOT NULL,
                    quantity INT NOT NULL DEFAULT 0,
                    unit_price DECIMAL(8,2) NOT NULL DEFAULT 0.00,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    CONSTRAINT fk_entry_detail_entry FOREIGN KEY (product_entry_id) 
                        REFERENCES product_entries(id) ON DELETE CASCADE,
                    CONSTRAINT fk_entry_detail_product FOREIGN KEY (product_id) 
                        REFERENCES products(id) ON DELETE RESTRICT,
                    INDEX idx_entry_details_entry (product_entry_id),
                    INDEX idx_entry_details_product (product_id)
                )
            """);
        }
    }
}