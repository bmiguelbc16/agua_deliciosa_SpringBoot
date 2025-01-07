package com.bances.agua_deliciosa.model;

import java.math.BigDecimal;

public class OrderDetail extends BaseModel {
    private Long orderId;
    private Long productId;
    private boolean active;
    private Order order;
    private Product product;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    // Getters
    public Long getOrderId() {
        return orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public boolean isActive() {
        return active;
    }

    public Order getOrder() {
        return order;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    // Setters
    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setOrder(Order order) {
        if (this.order != null && this.order != order) {
            this.order.removeDetail(this);
        }
        this.order = order;
        if (order != null) {
            order.addDetail(this);
        }
    }

    public void setProduct(Product product) {
        if (this.product != null && this.product != product) {
            this.product.removeOrderDetail(this);
        }
        this.product = product;
        if (product != null) {
            product.addOrderDetail(this);
        }
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateSubtotal();
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        calculateSubtotal();
    }

    protected void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    private void calculateSubtotal() {
        if (unitPrice != null && quantity > 0) {
            this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        } else {
            this.subtotal = BigDecimal.ZERO;
        }
    }

    // Métodos de utilidad
    public BigDecimal getTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDetail)) return false;
        OrderDetail that = (OrderDetail) o;
        return orderId != null && productId != null && 
               orderId.equals(that.orderId) && productId.equals(that.productId);
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
               ", productId=" + productId + ", active=" + active + 
               ", quantity=" + quantity + ", unitPrice=" + unitPrice + "]";
    }
}
