package com.bances.agua_deliciosa.dto.admin;

import java.time.LocalDate;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EmployeeDTO {
    private Long id;

    @NotBlank(message = "El nombre es requerido")
    private String name;

    @NotBlank(message = "El apellido es requerido")
    private String lastName;

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe ser válido")
    private String email;

    @NotBlank(message = "El número de documento es requerido")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
    private String documentNumber;

    @NotBlank(message = "El número de teléfono es requerido")
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener 9 dígitos")
    private String phoneNumber;

    @NotNull(message = "La fecha de nacimiento es requerida")
    private LocalDate birthDate;

    @NotBlank(message = "El género es requerido")
    private String gender;

    private String password;

    @NotBlank(message = "El rol es requerido")
    private String roles;
}
