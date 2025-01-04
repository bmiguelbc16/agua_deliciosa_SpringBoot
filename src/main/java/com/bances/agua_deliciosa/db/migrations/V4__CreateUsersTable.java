package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.stereotype.Component;

@Component
public class V4__CreateUsersTable implements JavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            // Crear tabla users
            statement.execute("""
                CREATE TABLE users (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    name VARCHAR(255) NOT NULL,
                    last_name VARCHAR(255) NOT NULL,
                    email VARCHAR(255) NOT NULL,
                    document_number VARCHAR(20),
                    birth_date DATE,
                    gender ENUM('M', 'F', 'O') NOT NULL,
                    phone_number VARCHAR(20),
                    password VARCHAR(255) NOT NULL,
                    email_verified_at TIMESTAMP NULL,
                    role_id BIGINT NOT NULL,
                    userable_type ENUM('Employee', 'Client') NOT NULL,
                    userable_id BIGINT NOT NULL,
                    active BOOLEAN NOT NULL DEFAULT TRUE,
                    guard_name VARCHAR(125) NOT NULL DEFAULT 'web',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    UNIQUE KEY unique_email (email),
                    UNIQUE KEY unique_userable (userable_type, userable_id),
                    INDEX idx_document (document_number),
                    INDEX idx_email (email),
                    CONSTRAINT fk_user_role FOREIGN KEY (role_id) 
                        REFERENCES roles (id) ON DELETE RESTRICT
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
            """);

            // Crear triggers para validar el userable_type
            statement.execute("""
                CREATE TRIGGER before_insert_user
                BEFORE INSERT ON users
                FOR EACH ROW
                BEGIN
                    IF NEW.userable_type = 'Employee' THEN
                        IF NOT EXISTS (SELECT 1 FROM employees WHERE id = NEW.userable_id) THEN
                            SIGNAL SQLSTATE '45000'
                            SET MESSAGE_TEXT = 'Invalid employee reference';
                        END IF;
                    ELSEIF NEW.userable_type = 'Client' THEN
                        IF NOT EXISTS (SELECT 1 FROM clients WHERE id = NEW.userable_id) THEN
                            SIGNAL SQLSTATE '45000'
                            SET MESSAGE_TEXT = 'Invalid client reference';
                        END IF;
                    END IF;
                END
            """);

            statement.execute("""
                CREATE TRIGGER before_update_user
                BEFORE UPDATE ON users
                FOR EACH ROW
                BEGIN
                    IF NEW.userable_type = 'Employee' THEN
                        IF NOT EXISTS (SELECT 1 FROM employees WHERE id = NEW.userable_id) THEN
                            SIGNAL SQLSTATE '45000'
                            SET MESSAGE_TEXT = 'Invalid employee reference';
                        END IF;
                    ELSEIF NEW.userable_type = 'Client' THEN
                        IF NOT EXISTS (SELECT 1 FROM clients WHERE id = NEW.userable_id) THEN
                            SIGNAL SQLSTATE '45000'
                            SET MESSAGE_TEXT = 'Invalid client reference';
                        END IF;
                    END IF;
                END
            """);

            // Crear tabla user_tokens
            statement.execute("""
                CREATE TABLE user_tokens (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    user_id BIGINT NOT NULL,
                    token VARCHAR(64) NOT NULL,
                    expires_at TIMESTAMP NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    UNIQUE KEY unique_token (token),
                    CONSTRAINT fk_user_tokens_user_id FOREIGN KEY (user_id) 
                        REFERENCES users(id) ON DELETE CASCADE
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
            """);
        }
    }

    @Override
    public boolean canExecuteInTransaction() { return true; }

    @Override
    public Integer getChecksum() { return null; }

    @Override
    public MigrationVersion getVersion() { 
        return MigrationVersion.fromVersion("4");
    }

    @Override
    public String getDescription() {
        return "Create users and user_tokens tables";
    }
}
