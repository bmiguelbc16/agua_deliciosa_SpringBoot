package com.bances.agua_deliciosa.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductEntry extends BaseModel {
    private Long employeeId;
    private Employee employee;
    private MovementType movementType;
    private String description;
    private List<ProductEntryDetail> details = new ArrayList<>();
    private boolean active;
    
    // Getters
    public Long getEmployeeId() {
        return employeeId;
    }
    
    public Employee getEmployee() {
        return employee;
    }
    
    public MovementType getMovementType() {
        return movementType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public List<ProductEntryDetail> getDetails() {
        return new ArrayList<>(details);
    }
    
    public boolean isActive() {
        return active;
    }
    
    // Setters
    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public void setDetails(List<ProductEntryDetail> details) {
        this.details = details != null ? new ArrayList<>(details) : new ArrayList<>();
    }
    
    // Métodos de utilidad
    public void addDetail(ProductEntryDetail detail) {
        if (detail != null) {
            details.add(detail);
            detail.setEntry(this);
        }
    }
    
    public void removeDetail(ProductEntryDetail detail) {
        if (detail != null && details.contains(detail)) {
            details.remove(detail);
            if (detail.getEntry() == this) {
                detail.setEntry(null);
            }
        }
    }
    
    public BigDecimal getTotal() {
        return details.stream()
                .map(ProductEntryDetail::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    @Override
    public String toString() {
        return "ProductEntry [id=" + getId() + ", employeeId=" + employeeId + 
               ", movementType=" + movementType + ", description=" + description + 
               ", active=" + active + ", details=" + details.size() + "]";
    }
}
