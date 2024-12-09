package com.bances.agua_deliciosa.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Data
public class ClientDTO {
    @NotBlank
    private String nombres;
    
    @NotBlank
    private String apellidos;
    
    @NotBlank
    @Size(min = 8, max = 8)
    private String dni;
    
    @Email
    private String email;
    
    @NotBlank
    private String password;
    
    private String telefono;
    private String direccion;
    private boolean active = true;
} 