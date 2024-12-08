package com.bances.agua_deliciosa.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "clientes")
@Data
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(nullable = false)
    private String nombres;
    
    @Column(nullable = false)
    private String apellidos;
    
    @Column(length = 8, nullable = false, unique = true)
    private String dni;
    
    @Column(length = 9)
    private String telefono;
    
    private String direccion;
} 