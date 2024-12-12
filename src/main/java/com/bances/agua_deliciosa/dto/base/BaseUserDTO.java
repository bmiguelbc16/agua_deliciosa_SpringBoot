package com.bances.agua_deliciosa.dto.base;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseUserDTO extends BaseDTO {
    
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String name;
    
    @NotBlank(message = "El apellido es requerido")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String lastName;
    
    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe ser válido")
    private String email;
    
    @NotBlank(message = "El número de documento es requerido")
    @Pattern(regexp = "\\d{8,11}", message = "El número de documento debe tener entre 8 y 11 dígitos")
    private String documentNumber;
    
    @Pattern(regexp = "\\d{9}", message = "El número de teléfono debe tener 9 dígitos")
    private String phoneNumber;
    
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;
    
    // Métodos helper
    public String getFullName() {
        return name + " " + lastName;
    }
} 