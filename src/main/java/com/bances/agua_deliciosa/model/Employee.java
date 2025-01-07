package com.bances.agua_deliciosa.model;

import java.util.ArrayList;
import java.util.List;

public class Employee extends BaseModel {
    private User user;
    private List<ProductOutput> productOutputs = new ArrayList<>();
    private List<ProductEntry> productEntries = new ArrayList<>();
    private List<StoreOrder> salesOrders = new ArrayList<>();
    private List<StoreOrder> deliveryOrders = new ArrayList<>();
    private List<WebOrder> webDeliveryOrders = new ArrayList<>();
    private List<PaymentProof> verifiedPayments = new ArrayList<>();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            user.setUserableType("App\\Models\\Employee");
            user.setUserableId(getId());
        }
    }

    public List<ProductOutput> getProductOutputs() {
        return new ArrayList<>(productOutputs);
    }

    public void setProductOutputs(List<ProductOutput> outputs) {
        this.productOutputs = outputs != null ? new ArrayList<>(outputs) : new ArrayList<>();
    }

    public List<ProductEntry> getProductEntries() {
        return new ArrayList<>(productEntries);
    }

    public void setProductEntries(List<ProductEntry> entries) {
        this.productEntries = entries != null ? new ArrayList<>(entries) : new ArrayList<>();
    }

    public List<StoreOrder> getSalesOrders() {
        return new ArrayList<>(salesOrders);
    }

    public void setSalesOrders(List<StoreOrder> orders) {
        this.salesOrders = orders != null ? new ArrayList<>(orders) : new ArrayList<>();
    }

    public List<StoreOrder> getDeliveryOrders() {
        return new ArrayList<>(deliveryOrders);
    }

    public void setDeliveryOrders(List<StoreOrder> orders) {
        this.deliveryOrders = orders != null ? new ArrayList<>(orders) : new ArrayList<>();
    }

    public List<WebOrder> getWebDeliveryOrders() {
        return new ArrayList<>(webDeliveryOrders);
    }

    public void setWebDeliveryOrders(List<WebOrder> orders) {
        this.webDeliveryOrders = orders != null ? new ArrayList<>(orders) : new ArrayList<>();
    }

    public List<PaymentProof> getVerifiedPayments() {
        return new ArrayList<>(verifiedPayments);
    }

    public void setVerifiedPayments(List<PaymentProof> payments) {
        this.verifiedPayments = payments != null ? new ArrayList<>(payments) : new ArrayList<>();
    }

    // Métodos de utilidad
    public void addProductOutput(ProductOutput output) {
        if (output != null) {
            productOutputs.add(output);
            output.setEmployee(this);
        }
    }

    public void addProductEntry(ProductEntry entry) {
        if (entry != null) {
            productEntries.add(entry);
            entry.setEmployee(this);
        }
    }

    public void addSalesOrder(StoreOrder order) {
        if (order != null) {
            salesOrders.add(order);
            order.setSellerEmployee(this);
        }
    }

    public void addDeliveryOrder(StoreOrder order) {
        if (order != null) {
            deliveryOrders.add(order);
            order.setDeliveryEmployee(this);
        }
    }

    public void addWebDeliveryOrder(WebOrder order) {
        if (order != null) {
            webDeliveryOrders.add(order);
            order.setDeliveryEmployee(this);
        }
    }

    public void addVerifiedPayment(PaymentProof payment) {
        if (payment != null) {
            verifiedPayments.add(payment);
            payment.setVerifiedBy(this);
        }
    }
}
