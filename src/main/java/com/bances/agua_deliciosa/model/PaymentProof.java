package com.bances.agua_deliciosa.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentProof extends BaseModel {
    private Long orderId;
    private PaymentType paymentType;
    private BigDecimal amount;
    private byte[] proofImage;
    private String imageName;
    private String imageType;
    private VerificationStatus verificationStatus;
    private Long verifiedByEmployeeId;
    private LocalDateTime verificationDate;
    private String verificationNotes;
    private Long createdByEmployeeId;

    public PaymentProof() {
        super();
        this.verificationStatus = VerificationStatus.PENDING;
        this.amount = BigDecimal.ZERO;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        this.amount = amount;
    }

    public byte[] getProofImage() {
        return proofImage;
    }

    public void setProofImage(byte[] proofImage) {
        this.proofImage = proofImage;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public Long getVerifiedByEmployeeId() {
        return verifiedByEmployeeId;
    }

    public void setVerifiedByEmployeeId(Long verifiedByEmployeeId) {
        this.verifiedByEmployeeId = verifiedByEmployeeId;
    }

    public LocalDateTime getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(LocalDateTime verificationDate) {
        this.verificationDate = verificationDate;
    }

    public String getVerificationNotes() {
        return verificationNotes;
    }

    public void setVerificationNotes(String verificationNotes) {
        this.verificationNotes = verificationNotes;
    }

    public Long getCreatedByEmployeeId() {
        return createdByEmployeeId;
    }

    public void setCreatedByEmployeeId(Long createdByEmployeeId) {
        this.createdByEmployeeId = createdByEmployeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentProof)) return false;
        PaymentProof that = (PaymentProof) o;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "PaymentProof [id=" + getId() + ", orderId=" + orderId + 
               ", paymentType=" + paymentType + ", amount=" + amount + 
               ", imageName=" + imageName + ", imageType=" + imageType + 
               ", verificationStatus=" + verificationStatus + 
               ", verifiedByEmployeeId=" + verifiedByEmployeeId + 
               ", verificationDate=" + verificationDate + 
               ", createdByEmployeeId=" + createdByEmployeeId + "]";
    }
}
