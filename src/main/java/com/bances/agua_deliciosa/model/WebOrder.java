package com.bances.agua_deliciosa.model;

public class WebOrder extends Order {
    
    private Employee deliveryEmployee;
    
    public WebOrder() {
        setOrderableType("App\\Models\\WebOrder");
        initializeOrderable();
    }
    
    public Employee getDeliveryEmployee() {
        return deliveryEmployee;
    }
    
    public void setDeliveryEmployee(Employee deliveryEmployee) {
        this.deliveryEmployee = deliveryEmployee;
    }
}
