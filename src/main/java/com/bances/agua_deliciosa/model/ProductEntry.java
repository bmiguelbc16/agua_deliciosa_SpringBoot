package com.bances.agua_deliciosa.model;

import java.math.BigDecimal;

public class ProductEntry extends BaseModel {
    private Long employeeId;
    private MovementType movementType;
    private String description;
    private BigDecimal totalAmount;

    public ProductEntry() {
        super();
        this.totalAmount = BigDecimal.ZERO;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductEntry)) return false;
        ProductEntry that = (ProductEntry) o;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ProductEntry [id=" + getId() + ", employeeId=" + employeeId + 
               ", movementType=" + movementType + ", description=" + description + 
               ", totalAmount=" + totalAmount + "]";
    }
}
