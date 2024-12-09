package com.bances.agua_deliciosa.db.migrations;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

@Component
public class V2__CreateUsersTable implements Migration {
    @Override
    public void migrate(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    document_number VARCHAR(11) NOT NULL UNIQUE,
                    name VARCHAR(255) NOT NULL,
                    last_name VARCHAR(255) NOT NULL,
                    birth_date DATE NOT NULL,
                    gender ENUM('M', 'F') NOT NULL,
                    phone_number VARCHAR(20),
                    email VARCHAR(255) NOT NULL UNIQUE,
                    userable_type VARCHAR(255) NOT NULL,
                    userable_id BIGINT NOT NULL,
                    email_verified_at TIMESTAMP NULL,
                    password VARCHAR(255) NOT NULL,
                    remember_token VARCHAR(100),
                    active BOOLEAN DEFAULT true,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    INDEX users_userable_type_userable_id_index (userable_type, userable_id)
                )
            """);
        }
    }
} 