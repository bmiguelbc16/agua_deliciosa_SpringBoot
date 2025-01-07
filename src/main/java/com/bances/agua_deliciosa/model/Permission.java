package com.bances.agua_deliciosa.model;

import java.util.HashSet;
import java.util.Set;

public class Permission extends BaseModel {
    
    private String name;
    private String description;
    private String guardName = "web";
    private boolean active = true;
    private Set<Role> roles = new HashSet<>();

    // Getters
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getGuardName() {
        return guardName;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public Set<Role> getRoles() {
        return new HashSet<>(roles);
    }
    
    // Setters
    public void setName(String name) {
        this.name = name;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setGuardName(String guardName) {
        this.guardName = guardName;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public void setRoles(Set<Role> roles) {
        this.roles = roles != null ? new HashSet<>(roles) : new HashSet<>();
    }
    
    // Métodos de utilidad
    public void addRole(Role role) {
        if (role != null) {
            roles.add(role);
            role.getPermissions().add(this);
        }
    }
    
    public void removeRole(Role role) {
        if (role != null) {
            roles.remove(role);
            role.getPermissions().remove(this);
        }
    }
    
    public boolean hasRole(String roleName) {
        return roles.stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission)) return false;
        Permission that = (Permission) o;
        return name != null && name.equals(that.name);
    }
    
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "Permission(" + name + ")";
    }
}
