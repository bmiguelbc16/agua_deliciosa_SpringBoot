package com.bances.agua_deliciosa.db.migrations;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

@Component
public class V5__CreateEmployeesTable implements Migration {
    @Override
    public void migrate(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS employees (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                )
            """);
        }
    }
}
 