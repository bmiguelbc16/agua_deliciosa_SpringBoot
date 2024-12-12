package com.bances.agua_deliciosa.dto.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientProfileDTO {
    @NotBlank(message = "El nombre es requerido")
    private String name;
    
    @NotBlank(message = "El apellido es requerido")
    private String lastName;
    
    @NotBlank(message = "El email es requerido")
    @Email(message = "Email inválido")
    private String email;
    
    @Pattern(regexp = "\\d{9}", message = "El número de teléfono debe tener 9 dígitos")
    private String phoneNumber;
    
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
} 