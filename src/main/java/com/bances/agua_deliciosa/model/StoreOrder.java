package com.bances.agua_deliciosa.model;

public class StoreOrder extends Order {
    
    private Employee sellerEmployee;
    private Employee deliveryEmployee;
    
    public StoreOrder() {
        setOrderableType("App\\Models\\StoreOrder");
        initializeOrderable();
    }
    
    public Employee getSellerEmployee() {
        return sellerEmployee;
    }
    
    public void setSellerEmployee(Employee sellerEmployee) {
        this.sellerEmployee = sellerEmployee;
    }
    
    public Employee getDeliveryEmployee() {
        return deliveryEmployee;
    }
    
    public void setDeliveryEmployee(Employee deliveryEmployee) {
        this.deliveryEmployee = deliveryEmployee;
    }
}
