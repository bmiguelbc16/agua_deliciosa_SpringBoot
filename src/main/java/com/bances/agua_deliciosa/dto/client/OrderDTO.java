package com.bances.agua_deliciosa.dto.client;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderDTO {
    @NotNull(message = "La cantidad es requerida")
    @Min(value = 1, message = "La cantidad mínima es 1")
    private Integer quantity;
    
    @NotNull(message = "La fecha de entrega es requerida")
    private LocalDateTime deliveryDate;
    
    private String address;
    private String notes;
} 