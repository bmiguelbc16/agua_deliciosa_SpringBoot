package com.bances.agua_deliciosa.service.system;

import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import com.bances.agua_deliciosa.db.migrations.Migration;

@Service
@RequiredArgsConstructor
public class DatabaseMigrationService {
    
    private final DataSource dataSource;
    private final List<Migration> migrations;
    
    @PostConstruct
    public void init() {
        executeMigrations();
    }
    
    private void executeMigrations() {
        try (Connection connection = dataSource.getConnection()) {
            for (Migration migration : migrations) {
                migration.migrate(connection);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error executing migrations", e);
        }
    }
} 