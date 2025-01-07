package com.bances.agua_deliciosa.model;

import java.time.LocalDateTime;

public class UserVerification extends BaseModel {
    private User user;
    private String token;
    private String verificationType;
    private LocalDateTime expiryDate;
    private boolean verified;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null && !user.getVerifications().contains(this)) {
            user.addVerification(this);
        }
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

    public boolean isExpired() {
        return expiryDate != null && LocalDateTime.now().isAfter(expiryDate);
    }

    public boolean isValid() {
        return !isExpired() && !verified;
    }

    public boolean canBeVerified() {
        return !isExpired() && !verified;
    }
}
