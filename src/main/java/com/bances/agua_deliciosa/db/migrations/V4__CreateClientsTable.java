package com.bances.agua_deliciosa.db.migrations;

import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

@Component
public class V4__CreateClientsTable implements Migration {
    @Override
    public void migrate(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS clients (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    address VARCHAR(255),
                    reference VARCHAR(255),
                    latitude DOUBLE,
                    longitude DOUBLE,
                    gender VARCHAR(1) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                )
            """);
        }
    }
} 