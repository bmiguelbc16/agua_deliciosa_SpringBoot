package com.bances.agua_deliciosa.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class Role extends BaseModel {
    
    private String name;
    private String description;
    private String guardName;
    private Set<Permission> permissions = new HashSet<>();

    public Role() {
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

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role that = (Role) o;
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
        return "Role [id=" + getId() + ", name=" + name + 
               ", description=" + description + ", guardName=" + guardName + "]";
    }
}
