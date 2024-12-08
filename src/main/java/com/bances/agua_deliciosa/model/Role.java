package com.bances.agua_deliciosa.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 50, nullable = false, unique = true)
    private String name;
    
    @Column(length = 100)
    private String description;
} 