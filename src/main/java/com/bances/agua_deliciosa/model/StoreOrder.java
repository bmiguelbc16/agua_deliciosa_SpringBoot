package com.bances.agua_deliciosa.model;

public class StoreOrder extends BaseModel {
    private Long sellerEmployeeId;
    private Long deliveryEmployeeId;

    public StoreOrder() {
        super();
    }

    public Long getSellerEmployeeId() {
        return sellerEmployeeId;
    }

    public void setSellerEmployeeId(Long sellerEmployeeId) {
        this.sellerEmployeeId = sellerEmployeeId;
    }

    public Long getDeliveryEmployeeId() {
        return deliveryEmployeeId;
    }

    public void setDeliveryEmployeeId(Long deliveryEmployeeId) {
        this.deliveryEmployeeId = deliveryEmployeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoreOrder)) return false;
        StoreOrder that = (StoreOrder) o;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "StoreOrder [id=" + getId() + ", sellerEmployeeId=" + sellerEmployeeId + 
               ", deliveryEmployeeId=" + deliveryEmployeeId + "]";
    }
}
