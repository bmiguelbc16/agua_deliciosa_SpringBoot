package com.bances.agua_deliciosa.model;

import com.bances.agua_deliciosa.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role extends BaseEntity {
    
    @Column(unique = true, nullable = false)
    private String name;
    
    @Column(name = "guard_name", nullable = false)
    private String guardName;
    
    @Column(name = "description", nullable = false)
    private String description;
    
    public String getDisplayName() {
        return name.replace("ROLE_", "");
    }
} 