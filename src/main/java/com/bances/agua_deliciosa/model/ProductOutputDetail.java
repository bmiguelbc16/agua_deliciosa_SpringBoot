package com.bances.agua_deliciosa.model;

import java.math.BigDecimal;

public class ProductOutputDetail extends BaseModel {
    private Long productOutputId;
    private Long productId;
    private int quantity;
    private BigDecimal unitPrice;

    public ProductOutputDetail() {
        super();
    }

    public Long getProductOutputId() {
        return productOutputId;
    }

    public void setProductOutputId(Long productOutputId) {
        this.productOutputId = productOutputId;
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
        if (!(o instanceof ProductOutputDetail)) return false;
        ProductOutputDetail that = (ProductOutputDetail) o;
        return productOutputId != null && productOutputId.equals(that.productOutputId) &&
               productId != null && productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        int result = productOutputId != null ? productOutputId.hashCode() : 0;
        result = 31 * result + (productId != null ? productId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductOutputDetail [id=" + getId() + ", productOutputId=" + productOutputId + 
               ", productId=" + productId + ", quantity=" + quantity + 
               ", unitPrice=" + unitPrice + "]";
    }
}
