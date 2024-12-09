package com.bances.agua_deliciosa.db.migrations;

import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

@Component
public class V14__CreateFailedJobsTable implements Migration {
    @Override
    public void migrate(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS failed_jobs (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    connection TEXT NOT NULL,
                    queue TEXT NOT NULL,
                    payload LONGTEXT NOT NULL,
                    exception LONGTEXT NOT NULL,
                    failed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    INDEX idx_failed_jobs_date (failed_at)
                )
            """);
        }
    }
}