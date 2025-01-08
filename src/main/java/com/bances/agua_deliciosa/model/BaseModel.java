package com.bances.agua_deliciosa.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BaseModel() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}