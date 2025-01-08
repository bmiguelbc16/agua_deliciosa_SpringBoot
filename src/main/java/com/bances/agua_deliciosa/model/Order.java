package com.bances.agua_deliciosa.model;

import java.time.LocalDate;
import java.math.BigDecimal;

public class Order extends BaseModel {
    private Long customerId;
    private String orderableType;
    private Long orderableId;
    private LocalDate deliveryDate;
    private String deliveryAddress;
    private String status;
    private BigDecimal total;

    public Order() {
        super();
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getOrderableType() {
        return orderableType;
    }

    public void setOrderableType(String orderableType) {
        this.orderableType = orderableType;
    }

    public Long getOrderableId() {
        return orderableId;
    }

    public void setOrderableId(Long orderableId) {
        this.orderableId = orderableId;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order that = (Order) o;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Order [id=" + getId() + ", customerId=" + customerId + 
               ", orderableType=" + orderableType + ", orderableId=" + orderableId + 
               ", deliveryDate=" + deliveryDate + ", deliveryAddress=" + deliveryAddress + 
               ", status=" + status + ", total=" + total + "]";
    }
}
