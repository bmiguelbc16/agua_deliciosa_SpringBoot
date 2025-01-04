package com.bances.agua_deliciosa.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PermissionDTO {
    private Long id;

    @NotBlank(message = "El nombre del permiso es requerido")
    private String name;

    @NotBlank(message = "La descripción del permiso es requerida")
    private String description;
}
