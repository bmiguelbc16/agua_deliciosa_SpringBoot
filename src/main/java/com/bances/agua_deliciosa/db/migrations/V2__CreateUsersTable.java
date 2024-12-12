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
                    name VARCHAR(255) NOT NULL,
                    last_name VARCHAR(255) NOT NULL,
                    email VARCHAR(255) NOT NULL UNIQUE,
                    document_number VARCHAR(20) UNIQUE,
                    birth_date DATE NOT NULL,
                    gender ENUM('M', 'F', 'O') NOT NULL,
                    phone_number VARCHAR(20),
                    password VARCHAR(255) NOT NULL,
                    email_verified_at TIMESTAMP NULL,
                    remember_token VARCHAR(100),
                    userable_type VARCHAR(50) NOT NULL,
                    userable_id BIGINT NOT NULL,
                    active BOOLEAN DEFAULT TRUE,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    INDEX idx_userable (userable_type, userable_id)
                )
            """);

            // Tabla pivote user_roles
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS user_roles (
                    user_id BIGINT NOT NULL,
                    role_id BIGINT NOT NULL,
                    PRIMARY KEY (user_id, role_id),
                    CONSTRAINT fk_ur_user_id FOREIGN KEY (user_id) 
                        REFERENCES users (id) ON DELETE CASCADE,
                    CONSTRAINT fk_ur_role_id FOREIGN KEY (role_id) 
                        REFERENCES roles (id) ON DELETE CASCADE
                )
            """);
        }
    }
} 