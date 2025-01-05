package com.bances.agua_deliciosa.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "employees")
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "userable_id", insertable = false, updatable = false)
    private User user;

    @OneToMany(mappedBy = "employee")
    private Set<Order> orders = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    private Set<ProductOutput> productOutputs = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    private Set<ProductEntry> productEntries = new HashSet<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @PostPersist
    protected void afterPersist() {
        if (user != null) {
            user.setUserableType("App\\Models\\Employee");
            user.setUserableId(this.id);
        }
    }
}
