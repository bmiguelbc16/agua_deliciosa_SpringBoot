package com.bances.agua_deliciosa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bances.agua_deliciosa.db.seeders.RoleSeeder;
import com.bances.agua_deliciosa.db.seeders.UserSeeder;
import com.bances.agua_deliciosa.db.migrations.*;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.sql.SQLException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import jakarta.annotation.PostConstruct;

@Service
@Slf4j
@RequiredArgsConstructor
public class DatabaseSeederService {
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private RoleSeeder roleSeeder;
    
    @Autowired
    private UserSeeder userSeeder;
    
    @Autowired
    private List<Migration> migrations;

    private void createMigrationsTable(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS migrations (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    migration VARCHAR(255) NOT NULL,
                    batch INT NOT NULL,
                    executed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);
        }
    }

    private boolean isMigrationExecuted(Connection connection, String migrationName) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(
                "SELECT COUNT(*) FROM migrations WHERE migration = '" + migrationName + "'"
            );
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    private void recordMigration(Connection connection, String migrationName) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(
                "INSERT INTO migrations (migration, batch) VALUES ('" + migrationName + "', 1)"
            );
        }
    }

    private void executeInOrder(Connection connection, String migrationName) throws SQLException {
        if (!isMigrationExecuted(connection, migrationName)) {
            migrations.stream()
                .filter(m -> m.getClass().getSimpleName().equals(migrationName))
                .findFirst()
                .ifPresent(migration -> {
                    try {
                        log.info("Executing migration: {}", migrationName);
                        migration.migrate(connection);
                        recordMigration(connection, migrationName);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
        } else {
            log.info("Migration already executed: {}", migrationName);
        }
    }
    
    @PostConstruct
    public void seedDatabase() {
        try (Connection connection = DataSourceUtils.getConnection(dataSource)) {
            // Crear tabla de migraciones si no existe
            createMigrationsTable(connection);

            // Ejecutar migraciones en orden específico
            executeInOrder(connection, "V1__CreatePermissionTables");
            executeInOrder(connection, "V2__CreateUsersTable");
            executeInOrder(connection, "V3__CreatePasswordResetsTable");
            executeInOrder(connection, "V4__CreateClientsTable");
            executeInOrder(connection, "V5__CreateEmployeesTable");
            executeInOrder(connection, "V6__CreateProductsTable");
            executeInOrder(connection, "V7__CreateOrdersTable");
            executeInOrder(connection, "V8__CreateOrderDetailsTable");
            executeInOrder(connection, "V9__CreateOrderMovementsTable");
            executeInOrder(connection, "V10__CreateProductEntriesTable");
            executeInOrder(connection, "V11__CreateProductOutputsTable");
            executeInOrder(connection, "V12__CreateProductEntryDetailsTable");
            executeInOrder(connection, "V13__CreateProductOutputDetailsTable");
            executeInOrder(connection, "V14__CreateFailedJobsTable");
            executeInOrder(connection, "V15__CreatePersonalAccessTokensTable");

            // Verificar y ejecutar seeders
            if (isTableEmpty(connection, "roles")) {
                log.info("Seeding roles table...");
                roleSeeder.seed();
            }
            
            if (isTableEmpty(connection, "users")) {
                log.info("Seeding users table...");
                userSeeder.seed();
            }
            
        } catch (SQLException e) {
            log.error("Error seeding database", e);
            throw new RuntimeException("Failed to seed database", e);
        }
    }

    private boolean isTableEmpty(Connection connection, String tableName) {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName);
            rs.next();
            return rs.getInt(1) == 0;
        } catch (SQLException e) {
            log.warn("Error checking if table {} is empty: {}", tableName, e.getMessage());
            return true;
        }
    }
} 