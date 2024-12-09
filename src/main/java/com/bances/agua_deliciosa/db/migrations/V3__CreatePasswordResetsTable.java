package com.bances.agua_deliciosa.db.migrations;

import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

@Component
public class V3__CreatePasswordResetsTable implements Migration {
    @Override
    public void migrate(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS password_resets (
                    email VARCHAR(100),
                    token VARCHAR(100),
                    created_at TIMESTAMP NULL,
                    INDEX password_resets_email_index (email)
                )
            """);
        }
    }
} 