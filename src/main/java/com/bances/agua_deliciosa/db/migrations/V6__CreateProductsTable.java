package com.bances.agua_deliciosa.db.migrations;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

@Component
public class V6__CreateProductsTable implements Migration {
    @Override
    public void migrate(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS products (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    stock INT NOT NULL DEFAULT 0,
                    unit_price DECIMAL(8,2) NOT NULL DEFAULT 0.00,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    INDEX idx_products_name (name)
                )
            """);
        }
    }
}