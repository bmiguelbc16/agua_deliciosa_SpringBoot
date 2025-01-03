package com.bances.agua_deliciosa.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.FetchType;
import org.hibernate.annotations.JoinFormula;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.ConstraintMode;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "document_number", unique = true)
    private String documentNumber;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "gender", nullable = false)
    private String gender; // 'M', 'F', 'O'

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Column(name = "email_verified_at")
    private LocalDateTime emailVerifiedAt;

    @Column(name = "userable_type")
    private String userableType;

    @Column(name = "userable_id")
    private Long userableId;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relación uno a uno con Role
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserToken> tokens;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userable_id", insertable = false, updatable = false, 
                foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JoinFormula("CASE WHEN userable_type = 'Employee' THEN userable_id ELSE null END")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userable_id", insertable = false, updatable = false,
                foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JoinFormula("CASE WHEN userable_type = 'Client' THEN userable_id ELSE null END")
    private Client client;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
