package com.bances.agua_deliciosa.model;

public class OrderMovementDetail extends BaseModel {
    private Long orderId;
    private Long movementId;

    public OrderMovementDetail() {
        super();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getMovementId() {
        return movementId;
    }

    public void setMovementId(Long movementId) {
        this.movementId = movementId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderMovementDetail)) return false;
        OrderMovementDetail that = (OrderMovementDetail) o;
        return orderId != null && orderId.equals(that.orderId) &&
               movementId != null && movementId.equals(that.movementId);
    }

    @Override
    public int hashCode() {
        int result = orderId != null ? orderId.hashCode() : 0;
        result = 31 * result + (movementId != null ? movementId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OrderMovementDetail [id=" + getId() + ", orderId=" + orderId + 
               ", movementId=" + movementId + "]";
    }
}
