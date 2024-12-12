package com.bances.agua_deliciosa.model;

import com.bances.agua_deliciosa.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Getter
@Setter
public class Employee extends BaseEntity {
    
    @Column(name = "position", nullable = false)
    private String position;
    
    @Column(name = "salary")
    private BigDecimal salary;
    
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;
    
    @OneToOne(mappedBy = "employee")
    private User user;
} 