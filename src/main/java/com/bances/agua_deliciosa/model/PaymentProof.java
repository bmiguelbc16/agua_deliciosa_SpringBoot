package com.bances.agua_deliciosa.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentProof extends BaseModel {
    private Order order;
    private Employee verifiedBy;
    private String proofPath;
    private BigDecimal amount;
    private LocalDateTime verifiedAt;
    private String notes;
    private boolean verified;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Employee getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(Employee verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public String getProofPath() {
        return proofPath;
    }

    public void setProofPath(String proofPath) {
        this.proofPath = proofPath;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
        if (verified && verifiedAt == null) {
            this.verifiedAt = LocalDateTime.now();
        }
    }
}
