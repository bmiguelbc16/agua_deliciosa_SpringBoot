package com.bances.agua_deliciosa.model;

import com.bances.agua_deliciosa.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends BaseEntity {
    
    @Column(name = "client_id", nullable = false)
    private Long clientId;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(name = "delivery_date", nullable = false)
    private LocalDateTime deliveryDate;
    
    private String address;
    
    private String notes;
} 