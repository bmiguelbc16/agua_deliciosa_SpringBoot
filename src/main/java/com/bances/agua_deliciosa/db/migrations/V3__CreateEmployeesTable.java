package com.bances.agua_deliciosa.db.migrations;

import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

@Component
public class V3__CreateEmployeesTable implements Migration {
    @Override
    public void migrate(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS employees (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    position VARCHAR(100) NOT NULL,
                    salary DECIMAL(10,2),
                    hire_date DATE NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    CONSTRAINT fk_employee_user FOREIGN KEY (id) 
                        REFERENCES users(userable_id) 
                        ON DELETE CASCADE 
                        ON UPDATE CASCADE
                )
            """);
        }
    }
} 