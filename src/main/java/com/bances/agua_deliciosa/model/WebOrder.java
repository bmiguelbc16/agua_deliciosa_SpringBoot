package com.bances.agua_deliciosa.model;

public class WebOrder extends BaseModel {
    private Long deliveryEmployeeId;

    public WebOrder() {
        super();
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
        if (!(o instanceof WebOrder)) return false;
        WebOrder that = (WebOrder) o;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "WebOrder [id=" + getId() + ", deliveryEmployeeId=" + deliveryEmployeeId + "]";
    }
}
