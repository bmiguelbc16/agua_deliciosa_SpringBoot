package com.bances.agua_deliciosa.model;

import java.math.BigDecimal;

public class ProductOutputDetail extends BaseModel {
    private Long productOutputId;
    private Long productId;
    private ProductOutput output;
    private Product product;
    private int quantity;
    private BigDecimal unitPrice;
    private String notes;
    private boolean active;
    
    // Getters
    public Long getProductOutputId() {
        return productOutputId;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public ProductOutput getOutput() {
        return output;
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
    
    public String getNotes() {
        return notes;
    }
    
    public boolean isActive() {
        return active;
    }
    
    // Setters
    public void setProductOutputId(long productOutputId) {
        this.productOutputId = productOutputId;
    }
    
    public void setProductId(long productId) {
        this.productId = productId;
    }
    
    public void setOutput(ProductOutput output) {
        if (this.output != null && this.output != output) {
            this.output.removeDetail(this);
        }
        this.output = output;
        if (output != null) {
            output.addDetail(this);
        }
    }
    
    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            this.unitPrice = product.getSalePrice();
        }
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        if (product != null) {
            product.updateStock(-quantity);
        }
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    // Métodos de utilidad
    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
    
    public boolean hasAvailableStock() {
        return product != null && product.hasAvailableStock(quantity);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductOutputDetail)) return false;
        ProductOutputDetail that = (ProductOutputDetail) o;
        return output != null && product != null && 
               output.equals(that.output) && product.equals(that.product);
    }
    
    @Override
    public int hashCode() {
        int result = output != null ? output.hashCode() : 0;
        result = 31 * result + (product != null ? product.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "ProductOutputDetail [id=" + getId() + ", productOutputId=" + productOutputId + 
               ", productId=" + productId + ", quantity=" + quantity + 
               ", unitPrice=" + unitPrice + ", active=" + active + "]";
    }
}
