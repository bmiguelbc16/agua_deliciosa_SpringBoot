package com.bances.agua_deliciosa.db.migrations;

import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

@Component
public class V10__CreateProductEntriesTable implements Migration {
    @Override
    public void migrate(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS product_entries (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    entry_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    notes TEXT,
                    supplier_name VARCHAR(100) NOT NULL,
                    supplier_ruc VARCHAR(20),
                    is_canceled BOOLEAN NOT NULL DEFAULT false,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    INDEX idx_entries_date (entry_date),
                    INDEX idx_entries_supplier (supplier_name)
                )
            """);
        }
    }
}