package com.bances.agua_deliciosa.dto.admin.summary;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bances.agua_deliciosa.model.OrderStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderSummaryDTO {
    private Long id;
    private String orderNumber;
    private String clientName;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private String statusClass;
    private LocalDateTime createdAt;
}
