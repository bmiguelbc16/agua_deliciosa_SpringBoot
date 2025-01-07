package com.bances.agua_deliciosa.model;

import java.util.HashSet;
import java.util.Set;

public class Role extends BaseModel {
    
    private String name;
    private String description;
    private String guardName = "web";
    private boolean active;
    private Set<Permission> permissions = new HashSet<>();
    private Set<User> users = new HashSet<>();

    // Getters y Setters
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

    public Set<Permission> getPermissions() {
        return new HashSet<>(permissions);
    }
    
    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions != null ? new HashSet<>(permissions) : new HashSet<>();
    }

    public Set<User> getUsers() {
        return new HashSet<>(users);
    }

    public void setUsers(Set<User> users) {
        this.users = users != null ? new HashSet<>(users) : new HashSet<>();
    }

    // Métodos de utilidad
    public void addPermission(Permission permission) {
        if (permission != null) {
            permissions.add(permission);
            permission.getRoles().add(this);
        }
    }

    public void removePermission(Permission permission) {
        if (permission != null) {
            permissions.remove(permission);
            permission.getRoles().remove(this);
        }
    }

    public void addUser(User user) {
        if (user != null) {
            users.add(user);
            user.setRole(this);
        }
    }

    public void removeUser(User user) {
        if (user != null) {
            users.remove(user);
            if (user.getRole() == this) {
                user.setRole(null);
            }
        }
    }

    public boolean hasPermission(String permissionName) {
        return permissions.stream()
                .anyMatch(permission -> permission.getName().equals(permissionName) && permission.isActive());
    }
}
