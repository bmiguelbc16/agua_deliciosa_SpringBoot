package com.bances.agua_deliciosa.model;

import java.time.LocalDateTime;

public class PasswordReset extends BaseModel {
    private Long userId;
    private String token;
    private LocalDateTime expiresAt;

    public PasswordReset() {
        super();
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

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PasswordReset)) return false;
        PasswordReset that = (PasswordReset) o;
        return token != null && token.equals(that.token);
    }

    @Override
    public int hashCode() {
        return token != null ? token.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "PasswordReset [id=" + getId() + ", userId=" + userId + 
               ", token=" + token + ", expiresAt=" + expiresAt + "]";
    }
}
