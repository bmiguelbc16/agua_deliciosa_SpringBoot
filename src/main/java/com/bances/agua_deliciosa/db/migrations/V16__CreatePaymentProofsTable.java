package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.stereotype.Component;

/**
 * Migración para crear la tabla de comprobantes de pago.
 * 
 * ESTRUCTURA:
 * ----------
 * Registro de comprobantes de pago:
 * - order_id: Pedido al que pertenece
 * - payment_type: Tipo de pago (YAPE, PLIN, CASH)
 * - amount: Monto del comprobante
 * - proof_image: Foto del comprobante
 * - verification_status: Estado de verificación
 *   * PENDING: Pendiente de revisión
 *   * VALID: Comprobante válido
 *   * INVALID: Comprobante inválido/falso
 * - verified_by: Empleado que verificó
 * - verification_notes: Notas de verificación
 * 
 * EJEMPLOS:
 * --------
 * 1. Comprobante Yape:
 *    - payment_type: "YAPE"
 *    - amount: 50.00
 *    - verification_status: "VALID"
 *    - verification_notes: "Verificado en estado de cuenta"
 * 
 * 2. Comprobante falso:
 *    - payment_type: "PLIN"
 *    - amount: 30.00
 *    - verification_status: "INVALID"
 *    - verification_notes: "No aparece en estado de cuenta"
 */
@Component
public class V16__CreatePaymentProofsTable implements JavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("""
                CREATE TABLE payment_proofs (
                    id BIGINT NOT NULL AUTO_INCREMENT,
                    order_id BIGINT NOT NULL,
                    payment_type ENUM('YAPE', 'PLIN', 'CASH') NOT NULL,
                    amount DECIMAL(10,2) NOT NULL,
                    proof_image MEDIUMBLOB NOT NULL,
                    image_name VARCHAR(255) NOT NULL,
                    image_type VARCHAR(100) NOT NULL,
                    verification_status ENUM('PENDING', 'VALID', 'INVALID') NOT NULL DEFAULT 'PENDING',
                    verified_by_employee_id BIGINT,
                    verification_date TIMESTAMP,
                    verification_notes TEXT,
                    created_by_employee_id BIGINT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    PRIMARY KEY (id),
                    INDEX idx_order (order_id),
                    INDEX idx_payment_type (payment_type),
                    INDEX idx_verification_status (verification_status),
                    INDEX idx_verified_by (verified_by_employee_id),
                    INDEX idx_created_by (created_by_employee_id),
                    CONSTRAINT fk_payment_proofs_order
                        FOREIGN KEY (order_id) REFERENCES orders(id),
                    CONSTRAINT fk_payment_proofs_verified_by
                        FOREIGN KEY (verified_by_employee_id) REFERENCES employees(id),
                    CONSTRAINT fk_payment_proofs_created_by
                        FOREIGN KEY (created_by_employee_id) REFERENCES employees(id),
                    CONSTRAINT chk_amount CHECK (amount > 0)
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
        return MigrationVersion.fromVersion("16");
    }

    @Override
    public String getDescription() { 
        return "Create payment proofs table";
    }
}
