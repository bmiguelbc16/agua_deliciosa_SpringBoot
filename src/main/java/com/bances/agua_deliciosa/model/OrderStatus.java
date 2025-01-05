package com.bances.agua_deliciosa.model;

public enum OrderStatus {
    PENDING,        // Pedido registrado pero no procesado
    IN_PROCESS,      // Pedido confirmado y en proceso
    IN_DELIVERY,    // Pedido en ruta de entrega
    DELIVERED,      // Pedido entregado al cliente
    CANCELLED      // Pedido cancelado
}
