package com.bances.agua_deliciosa.model;

import java.time.LocalDateTime;

public class Permission extends BaseModel {
    
    private String name;
    private String description;
    private String guardName;
    private boolean active;
    private Long roleId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Permission() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGuardName() {
        return guardName;
    }

    public void setGuardName(String guardName) {
        this.guardName = guardName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission)) return false;
        Permission that = (Permission) o;
        return name != null && name.equals(that.name) && 
               guardName != null && guardName.equals(that.guardName);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (guardName != null ? guardName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Permission [id=" + getId() + ", name=" + name + 
               ", description=" + description + ", guardName=" + guardName + 
               ", active=" + active + ", roleId=" + roleId + "]";
    }
}
