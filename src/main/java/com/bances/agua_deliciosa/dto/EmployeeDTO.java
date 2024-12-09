package com.bances.agua_deliciosa.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Data
public class EmployeeDTO {
    @NotBlank
    private String name;
    
    @NotBlank
    private String lastName;
    
    @NotBlank
    @Size(min = 8, max = 8)
    private String dni;
    
    private String telefono;
    
    @NotBlank
    private String gender;
    
    @Email
    private String email;
    
    @NotBlank
    private String password;
    
    private String role;
    
    private boolean active = true;
} 