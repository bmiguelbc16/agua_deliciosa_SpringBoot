package com.bances.agua_deliciosa.model;

import java.util.ArrayList;
import java.util.List;

public class OrderMovement extends BaseModel {
    
    private Long orderId;
    private Long employeeId;
    private MovementType movementType;
    private boolean active;
    
    private String title;
    
    private String description;
    
    private List<OrderMovementDetail> details = new ArrayList<>();
    
    // Constructor
    public OrderMovement() {
        super();
    }
    
    // Getters
    public Long getOrderId() {
        return orderId;
    }
    
    public Long getEmployeeId() {
        return employeeId;
    }
    
    public MovementType getMovementType() {
        return movementType;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public List<OrderMovementDetail> getDetails() {
        return details;
    }
    
    // Setters
    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
    
    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }
    
    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    // Métodos de utilidad
    public void addDetail(OrderMovementDetail detail) {
        if (detail != null && !details.contains(detail)) {
            details.add(detail);
            if (detail.getMovement() != this) {
                detail.setMovement(this);
            }
        }
    }
    
    public void removeDetail(OrderMovementDetail detail) {
        if (detail != null && details.contains(detail)) {
            details.remove(detail);
            if (detail.getMovement() == this) {
                detail.setMovement(null);
            }
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderMovement)) return false;
        OrderMovement that = (OrderMovement) o;
        return title != null && title.equals(that.title);
    }
    
    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "OrderMovement [id=" + getId() + ", orderId=" + orderId + 
               ", employeeId=" + employeeId + ", movementType=" + movementType + 
               ", active=" + active + ", details=" + details.size() + "]";
    }
}
