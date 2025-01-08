package com.bances.agua_deliciosa.model;

import java.math.BigDecimal;

public class ProductEntryDetail extends BaseModel {
    private Long productEntryId;
    private Long productId;
    private int quantity;
    private BigDecimal unitPrice;

    public ProductEntryDetail() {
        super();
    }

    public Long getProductEntryId() {
        return productEntryId;
    }

    public void setProductEntryId(Long productEntryId) {
        this.productEntryId = productEntryId;
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
        if (!(o instanceof ProductEntryDetail)) return false;
        ProductEntryDetail that = (ProductEntryDetail) o;
        return productEntryId != null && productEntryId.equals(that.productEntryId) &&
               productId != null && productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        int result = productEntryId != null ? productEntryId.hashCode() : 0;
        result = 31 * result + (productId != null ? productId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductEntryDetail [id=" + getId() + ", productEntryId=" + productEntryId + 
               ", productId=" + productId + ", quantity=" + quantity + 
               ", unitPrice=" + unitPrice + "]";
    }
}
