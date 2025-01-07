package com.bances.agua_deliciosa.model;

import java.math.BigDecimal;

public class ProductEntryDetail extends BaseModel {
    private Long productEntryId;
    private Long productId;
    private ProductEntry entry;
    private Product product;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private boolean active;
    
    // Getters
    public Long getProductEntryId() {
        return productEntryId;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public ProductEntry getEntry() {
        return entry;
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
    
    public boolean isActive() {
        return active;
    }
    
    // Setters
    public void setProductEntryId(long productEntryId) {
        this.productEntryId = productEntryId;
    }
    
    public void setProductId(long productId) {
        this.productId = productId;
    }
    
    public void setEntry(ProductEntry entry) {
        if (this.entry != null && this.entry != entry) {
            this.entry.removeDetail(this);
        }
        this.entry = entry;
        if (entry != null) {
            entry.addDetail(this);
        }
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        if (product != null) {
            product.updateStock(quantity);
        }
        calculateSubtotal();
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        calculateSubtotal();
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    // Métodos de utilidad
    private void calculateSubtotal() {
        if (unitPrice != null) {
            this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
    
    @Override
    public String toString() {
        return "ProductEntryDetail [id=" + getId() + ", productEntryId=" + productEntryId + 
               ", productId=" + productId + ", quantity=" + quantity + 
               ", unitPrice=" + unitPrice + ", subtotal=" + subtotal + 
               ", active=" + active + "]";
    }
}
