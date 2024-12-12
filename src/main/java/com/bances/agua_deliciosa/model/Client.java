package com.bances.agua_deliciosa.model;

import com.bances.agua_deliciosa.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "clients")
@Getter
@Setter
public class Client extends BaseEntity {
    
    @Column
    private String address;
    
    @Column
    private String reference;
    
    @Column
    private Double latitude;
    
    @Column
    private Double longitude;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;
    
    @OneToOne(mappedBy = "client")
    private User user;
} 