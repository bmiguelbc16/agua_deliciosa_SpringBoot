package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.stereotype.Component;

@Component
public class V13__CreateProductOutputDetailsTable extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("""
                CREATE TABLE product_output_details (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    product_output_id BIGINT NOT NULL COMMENT 'ID de la salida',
                    product_id BIGINT NOT NULL COMMENT 'ID del producto',
                    quantity INT NOT NULL COMMENT 'Cantidad retirada',
                    unit_price DECIMAL(10,2) NOT NULL COMMENT 'Precio unitario de salida',
                    subtotal DECIMAL(10,2) NOT NULL COMMENT 'Cantidad * precio unitario',
                    lot_number VARCHAR(50) COMMENT 'Número de lote (si aplica)',
                    reason TEXT COMMENT 'Razón de la salida',
                    notes TEXT COMMENT 'Notas específicas del ítem',
                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    CONSTRAINT fk_product_output_details_output FOREIGN KEY (product_output_id) 
                        REFERENCES product_outputs(id) ON DELETE CASCADE,
                    CONSTRAINT fk_product_output_details_product FOREIGN KEY (product_id) 
                        REFERENCES products(id) ON DELETE RESTRICT,
                    CONSTRAINT chk_output_details_quantity CHECK (quantity > 0),
                    CONSTRAINT chk_output_details_price CHECK (unit_price >= 0),
                    INDEX idx_product_output_details_output (product_output_id),
                    INDEX idx_product_output_details_product (product_id),
                    INDEX idx_product_output_details_lot (lot_number)
                ) ENGINE=InnoDB COMMENT='Detalles de las salidas de productos'
            """);
        }
    }
}
