package com.bances.agua_deliciosa.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmPasswordDTO {
    
    @NotBlank(message = "La contraseña actual es requerida")
    private String currentPassword;
    
    @NotBlank(message = "La nueva contraseña es requerida")
    private String newPassword;
    
    @NotBlank(message = "La confirmación de contraseña es requerida")
    private String confirmPassword;
} 