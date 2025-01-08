package com.bances.agua_deliciosa.model;

import java.math.BigDecimal;

public class OrderDetail extends BaseModel {
    private Long orderId;
    private Long productId;
    private int quantity;
    private BigDecimal unitPrice;

    public OrderDetail() {
        super();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDetail)) return false;
        OrderDetail that = (OrderDetail) o;
        return orderId != null && orderId.equals(that.orderId) &&
               productId != null && productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        int result = orderId != null ? orderId.hashCode() : 0;
        result = 31 * result + (productId != null ? productId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OrderDetail [id=" + getId() + ", orderId=" + orderId + 
               ", productId=" + productId + ", quantity=" + quantity + 
               ", unitPrice=" + unitPrice + "]";
    }
}
