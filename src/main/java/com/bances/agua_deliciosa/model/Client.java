package com.bances.agua_deliciosa.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Client extends BaseModel {
    private String name;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private boolean active;
    private User user;
    private List<Order> orders = new ArrayList<>();
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public String getAddress() {
        return address;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public User getUser() {
        return user;
    }
    
    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }
    
    // Setters
    public void setName(String name) {
        this.name = name;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            user.setUserableType("App\\Models\\Client");
            user.setUserableId(getId());
        }
    }
    
    public void setOrders(List<Order> orders) {
        this.orders = orders != null ? new ArrayList<>(orders) : new ArrayList<>();
    }
    
    // Métodos de utilidad
    public void addOrder(Order order) {
        if (order != null) {
            orders.add(order);
            order.setCustomer(this);
        }
    }
    
    public void removeOrder(Order order) {
        if (order != null) {
            orders.remove(order);
            if (order.getCustomer() == this) {
                order.setCustomer(null);
            }
        }
    }
    
    public String getFullName() {
        return name + " " + lastName;
    }
    
    public int getTotalOrders() {
        return orders.size();
    }
    
    public List<Order> getPendingOrders() {
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.PENDING)
                .toList();
    }
    
    public List<Order> getCompletedOrders() {
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
                .toList();
    }
    
    public BigDecimal getTotalPendingOrders() {
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.PENDING)
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public BigDecimal getTotalCompletedOrders() {
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
