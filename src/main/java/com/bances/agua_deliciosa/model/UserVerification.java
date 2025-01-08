package com.bances.agua_deliciosa.model;

import java.time.LocalDateTime;

public class UserVerification extends BaseModel {
    private Long userId;
    private String token;
    private String verificationType;
    private LocalDateTime expiryDate;
    private boolean verified;

    public UserVerification() {
        super();
        this.verified = false;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVerificationType() {
        return verificationType;
    }

    public void setVerificationType(String verificationType) {
        this.verificationType = verificationType;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserVerification)) return false;
        UserVerification that = (UserVerification) o;
        return token != null && token.equals(that.token);
    }

    @Override
    public int hashCode() {
        return token != null ? token.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserVerification [id=" + getId() + ", userId=" + userId + 
               ", token=" + token + ", verificationType=" + verificationType + 
               ", expiryDate=" + expiryDate + ", verified=" + verified + "]";
    }
}
