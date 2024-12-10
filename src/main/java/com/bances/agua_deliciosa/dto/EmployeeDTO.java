package com.bances.agua_deliciosa.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import jakarta.validation.constraints.AssertTrue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Transient;

@Data
public class EmployeeDTO {
    @NotBlank(message = "El nombre es requerido")
    private String name;
    
    @NotBlank(message = "El apellido es requerido")
    private String lastName;
    
    @NotBlank(message = "El DNI es requerido")
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 dígitos")
    private String documentNumber;
    
    @NotNull(message = "La fecha de nacimiento es requerida")
    private LocalDate birthDate;
    
    @NotBlank(message = "El género es requerido")
    private String gender;
    
    private String phoneNumber;
    
    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe ser válido")
    private String email;
    
    private String password;
    
    @Transient // No se persiste en la base de datos
    private String confirmPassword;
    
    @NotBlank(message = "El rol es requerido")
    private String role;
    
    private boolean active = true;
    
    // Agregar validación personalizada
    @AssertTrue(message = "Las contraseñas no coinciden")
    @JsonIgnore
    public boolean isPasswordsMatch() {
        if (password == null || password.isEmpty()) {
            return true; // No validar si no se está cambiando la contraseña
        }
        return password.equals(confirmPassword);
    }
} 