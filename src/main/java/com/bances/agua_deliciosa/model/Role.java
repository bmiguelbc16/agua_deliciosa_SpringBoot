package com.bances.agua_deliciosa.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;

    @Column(name = "guard_name", nullable = false)
    private String guardName = "web";  // Valor por defecto

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Relación muchos a muchos con permisos (tabla intermedia role_has_permissions)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_has_permissions",  // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "role_id"), // Columna para los roles
            inverseJoinColumns = @JoinColumn(name = "permission_id") // Columna para los permisos
    )
    private Set<Permission> permissions;

    public Role() {}

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
