package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.stereotype.Component;

/**
 * Migración para crear la tabla de movimientos de pedidos.
 * 
 * La tabla order_movements almacena:
 * - Campos principales: order_id, user_id, status
 * - Campo opcional: notes
 * - Timestamps automáticos
 * 
 * Características:
 * - Índices en order_id y user_id
 * - Claves foráneas a orders y users
 * - Estado obligatorio
 * - Notas opcionales en formato TEXT
 * 
 * Se relaciona con:
 * - orders: pedido que se está rastreando
 * - users: usuario que registra el movimiento
 * Uso: Auditoría de cambios en pedidos
 */
@Component
public class V11__CreateOrderMovementsTable implements JavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("""
                CREATE TABLE order_movements (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    order_id BIGINT NOT NULL,
                    user_id BIGINT NOT NULL,
                    status VARCHAR(20) NOT NULL,
                    notes TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    INDEX idx_order_id (order_id),
                    INDEX idx_user_id (user_id),
                    CONSTRAINT fk_order_movements_order_id FOREIGN KEY (order_id) REFERENCES orders(id),
                    CONSTRAINT fk_order_movements_user_id FOREIGN KEY (user_id) REFERENCES users(id)
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
        return MigrationVersion.fromVersion("11");
    }

    @Override
    public String getDescription() { 
        return "Create order movements table";
    }
}
