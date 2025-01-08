package com.bances.agua_deliciosa.model;

import java.math.BigDecimal;

public class Product extends BaseModel {
    private String name;
    private String description;
    private BigDecimal salePrice;
    private boolean forSale;
    private int stock;

    public Product() {
        super();
        this.salePrice = BigDecimal.ZERO;
        this.forSale = false;
        this.stock = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public boolean isForSale() {
        return forSale;
    }

    public void setForSale(boolean forSale) {
        this.forSale = forSale;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product that = (Product) o;
        return name != null && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Product [id=" + getId() + ", name=" + name + 
               ", description=" + description + ", salePrice=" + salePrice + 
               ", forSale=" + forSale + ", stock=" + stock + "]";
    }
}
