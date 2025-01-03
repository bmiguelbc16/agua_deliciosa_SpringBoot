package com.bances.agua_deliciosa.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginDTO {
    @Email(message = "El correo debe ser válido")
    @NotEmpty(message = "El correo es obligatorio")
    private String email;

    @NotEmpty(message = "La contraseña es obligatoria")
    private String password;
}
