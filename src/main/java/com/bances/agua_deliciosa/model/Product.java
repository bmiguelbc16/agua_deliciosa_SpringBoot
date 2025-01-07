package com.bances.agua_deliciosa.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Product extends BaseModel {
    private String name;
    private String description;
    private BigDecimal salePrice;
    private boolean forSale;
    private int stock;
    private List<OrderDetail> orderDetails = new ArrayList<>();
    private List<ProductOutputDetail> outputDetails = new ArrayList<>();
    private List<ProductEntryDetail> entryDetails = new ArrayList<>();

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
        this.stock = stock;
    }

    public List<OrderDetail> getOrderDetails() {
        return new ArrayList<>(orderDetails);
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails != null ? new ArrayList<>(orderDetails) : new ArrayList<>();
    }

    public List<ProductOutputDetail> getOutputDetails() {
        return new ArrayList<>(outputDetails);
    }

    public void setOutputDetails(List<ProductOutputDetail> outputDetails) {
        this.outputDetails = outputDetails != null ? new ArrayList<>(outputDetails) : new ArrayList<>();
    }

    public List<ProductEntryDetail> getEntryDetails() {
        return new ArrayList<>(entryDetails);
    }

    public void setEntryDetails(List<ProductEntryDetail> entryDetails) {
        this.entryDetails = entryDetails != null ? new ArrayList<>(entryDetails) : new ArrayList<>();
    }

    public void addOrderDetail(OrderDetail detail) {
        if (detail != null) {
            orderDetails.add(detail);
            detail.setProduct(this);
        }
    }

    public void addOutputDetail(ProductOutputDetail detail) {
        if (detail != null) {
            outputDetails.add(detail);
            detail.setProduct(this);
        }
    }

    public void addEntryDetail(ProductEntryDetail detail) {
        if (detail != null) {
            entryDetails.add(detail);
            detail.setProduct(this);
        }
    }

    public void updateStock(int quantity) {
        this.stock += quantity;
    }
}