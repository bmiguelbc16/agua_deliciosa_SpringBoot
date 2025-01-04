package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.stereotype.Component;

@Component
public class V11__CreateProductEntriesTable extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("""
                CREATE TABLE product_entries (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    employee_id BIGINT NOT NULL COMMENT 'Empleado que registra la entrada',
                    entry_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha y hora de la entrada',
                    entry_type VARCHAR(50) NOT NULL DEFAULT 'PURCHASE' COMMENT 'Tipo: PURCHASE, RETURN, TRANSFER, ADJUSTMENT',
                    supplier_name VARCHAR(255) COMMENT 'Nombre del proveedor',
                    supplier_ruc VARCHAR(20) COMMENT 'RUC del proveedor',
                    invoice_number VARCHAR(50) COMMENT 'Número de factura o documento',
                    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT 'Monto total de la entrada',
                    payment_status VARCHAR(50) DEFAULT 'PENDING' COMMENT 'Estado del pago: PENDING, PAID, PARTIAL',
                    payment_due_date DATE COMMENT 'Fecha de vencimiento del pago',
                    notes TEXT COMMENT 'Notas o comentarios',
                    is_canceled BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Indica si la entrada fue cancelada',
                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    CONSTRAINT fk_product_entries_employee FOREIGN KEY (employee_id) 
                        REFERENCES employees(id) ON DELETE RESTRICT,
                    CONSTRAINT chk_product_entries_amount CHECK (total_amount >= 0),
                    INDEX idx_product_entries_employee (employee_id),
                    INDEX idx_product_entries_date (entry_date),
                    INDEX idx_product_entries_type (entry_type),
                    INDEX idx_product_entries_supplier (supplier_name, supplier_ruc),
                    INDEX idx_product_entries_payment (payment_status),
                    INDEX idx_product_entries_canceled (is_canceled)
                ) ENGINE=InnoDB COMMENT='Entradas de productos al inventario'
            """);
        }
    }
}
