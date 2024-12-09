package com.bances.agua_deliciosa.db.migrations;

import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

@Component
public class V15__CreatePersonalAccessTokensTable implements Migration {
    @Override
    public void migrate(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS personal_access_tokens (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    tokenable_type VARCHAR(255) NOT NULL,
                    tokenable_id BIGINT NOT NULL,
                    name VARCHAR(255) NOT NULL,
                    token VARCHAR(64) NOT NULL,
                    abilities TEXT,
                    last_used_at TIMESTAMP NULL,
                    expires_at TIMESTAMP NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    UNIQUE KEY idx_tokens_token (token),
                    INDEX idx_tokens_tokenable (tokenable_type, tokenable_id),
                    INDEX idx_tokens_name (name)
                )
            """);
        }
    }
}