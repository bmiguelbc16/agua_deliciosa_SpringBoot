package com.bances.agua_deliciosa.db.migrations;

import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

@Component
public class V5__CreatePasswordResetsTable implements Migration {
    @Override
    public void migrate(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS password_resets (
                    email VARCHAR(255) NOT NULL,
                    token VARCHAR(255) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    PRIMARY KEY (email, token),
                    FOREIGN KEY (email) REFERENCES users(email) ON DELETE CASCADE
                )
            """);
        }
    }
} 