package com.bances.agua_deliciosa.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "permissions")
@Getter
@Setter
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;  // Nombre del permiso, por ejemplo "VIEW_DASHBOARD"

    @Column
    private String description;  // Descripción del permiso

    @Column(name = "guard_name", nullable = false)
    private String guardName = "web";  // Valor por defecto para el guard

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Relación muchos a muchos con roles (tabla intermedia role_has_permissions)
    @ManyToMany
    @JoinTable(
            name = "role_has_permissions", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "permission_id"), // Columna para permisos
            inverseJoinColumns = @JoinColumn(name = "role_id") // Columna para roles
    )
    private Set<Role> roles;

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
