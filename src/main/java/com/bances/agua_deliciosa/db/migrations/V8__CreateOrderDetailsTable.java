package com.bances.agua_deliciosa.db.migrations;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.stereotype.Component;

@Component
public class V8__CreateOrderDetailsTable extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (var statement = context.getConnection().createStatement()) {
            statement.execute("""
                CREATE TABLE order_details (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    order_id BIGINT NOT NULL COMMENT 'ID del pedido',
                    product_id BIGINT NOT NULL COMMENT 'ID del producto',
                    quantity INT NOT NULL COMMENT 'Cantidad solicitada',
                    unit_price DECIMAL(10,2) NOT NULL COMMENT 'Precio unitario al momento de la venta',
                    subtotal DECIMAL(10,2) NOT NULL COMMENT 'Cantidad * precio unitario',
                    discount DECIMAL(10,2) DEFAULT 0 COMMENT 'Descuento aplicado al ítem',
                    notes TEXT COMMENT 'Notas específicas del ítem',
                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    CONSTRAINT fk_order_details_order FOREIGN KEY (order_id) 
                        REFERENCES orders(id) ON DELETE CASCADE,
                    CONSTRAINT fk_order_details_product FOREIGN KEY (product_id) 
                        REFERENCES products(id) ON DELETE RESTRICT,
                    CONSTRAINT chk_order_details_quantity CHECK (quantity > 0),
                    CONSTRAINT chk_order_details_price CHECK (unit_price >= 0),
                    CONSTRAINT chk_order_details_discount CHECK (discount >= 0 AND discount <= subtotal),
                    INDEX idx_order_details_order (order_id),
                    INDEX idx_order_details_product (product_id)
                ) ENGINE=InnoDB COMMENT='Detalles de los pedidos'
            """);
        }
    }
}
