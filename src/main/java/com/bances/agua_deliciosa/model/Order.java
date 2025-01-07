package com.bances.agua_deliciosa.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Order extends BaseModel {
    private Long clientId;
    private String code;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private OrderStatus status;
    private String notes;
    private LocalDate deliveryDate;
    private String deliveryAddress;
    private boolean active;
    private Client client;
    private Employee createdBy;
    private String orderableType;
    private Long orderableId;
    private List<OrderDetail> details = new ArrayList<>();
    private List<OrderMovementDetail> movements = new ArrayList<>();
    private List<PaymentProof> paymentProofs = new ArrayList<>();

    // Getters
    public Long getClientId() {
        return clientId;
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public boolean isActive() {
        return active;
    }

    public Client getClient() {
        return client;
    }

    public Employee getCreatedBy() {
        return createdBy;
    }

    public String getOrderableType() {
        return orderableType;
    }

    public Long getOrderableId() {
        return orderableId;
    }

    public List<OrderDetail> getDetails() {
        return new ArrayList<>(details);
    }

    public List<OrderMovementDetail> getMovements() {
        return new ArrayList<>(movements);
    }

    public List<PaymentProof> getPaymentProofs() {
        return new ArrayList<>(paymentProofs);
    }

    // Setters
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setClient(Client client) {
        if (this.client != null && this.client != client) {
            this.client.removeOrder(this);
        }
        this.client = client;
        if (client != null) {
            client.addOrder(this);
        }
    }

    public void setCreatedBy(Employee createdBy) {
        if (this.createdBy != null && this.createdBy != createdBy) {
            this.createdBy.removeCreatedOrder(this);
        }
        this.createdBy = createdBy;
        if (createdBy != null) {
            createdBy.addCreatedOrder(this);
        }
    }

    public void setOrderableType(String orderableType) {
        this.orderableType = orderableType;
    }

    public void setOrderableId(Long orderableId) {
        this.orderableId = orderableId;
    }

    public void setDetails(List<OrderDetail> details) {
        this.details = details != null ? new ArrayList<>(details) : new ArrayList<>();
    }

    public void setMovements(List<OrderMovementDetail> movements) {
        this.movements = movements != null ? new ArrayList<>(movements) : new ArrayList<>();
    }

    public void setPaymentProofs(List<PaymentProof> paymentProofs) {
        this.paymentProofs = paymentProofs != null ? new ArrayList<>(paymentProofs) : new ArrayList<>();
    }

    // Métodos de utilidad
    public void addDetail(OrderDetail detail) {
        if (detail != null) {
            details.add(detail);
            if (detail.getOrder() != this) {
                detail.setOrder(this);
            }
        }
    }

    public void addMovement(OrderMovementDetail movement) {
        if (movement != null) {
            movements.add(movement);
            movement.setOrder(this);
        }
    }

    public void addPaymentProof(PaymentProof proof) {
        if (proof != null) {
            paymentProofs.add(proof);
            if (proof.getOrder() != this) {
                proof.setOrder(this);
            }
        }
    }

    public void removeDetail(OrderDetail detail) {
        if (detail != null) {
            details.remove(detail);
            if (detail.getOrder() == this) {
                detail.setOrder(null);
            }
        }
    }

    public void removePaymentProof(PaymentProof proof) {
        if (proof != null) {
            paymentProofs.remove(proof);
            if (proof.getOrder() == this) {
                proof.setOrder(null);
            }
        }
    }

    public BigDecimal calculateTotal() {
        return details.stream()
                .map(detail -> detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean canBeDelivered() {
        return status == OrderStatus.PENDING && 
               !deliveryAddress.isEmpty() &&
               paymentProofs.stream()
                   .filter(PaymentProof::isValid)
                   .map(PaymentProof::getAmount)
                   .reduce(BigDecimal.ZERO, BigDecimal::add)
                   .compareTo(total) >= 0;
    }

    protected void initializeOrderable() {
        if (getId() != null) {
            setOrderableId(getId());
        }
    }
}
