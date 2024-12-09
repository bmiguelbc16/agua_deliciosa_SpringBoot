package com.bances.agua_deliciosa.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Where;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;


@Data
@Entity
@Table(name = "clients")
public class Client {
    public static final Map<String, String> OPTIONS_GENDER = Map.of(
        "M", "MASCULINO",
        "F", "FEMENINO"
    );
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "userable_id", 
                insertable = false, updatable = false)
    @Where(clause = "userable_type = 'Client'")
    private User user;
    
    @Column(name = "created_at")
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
} 