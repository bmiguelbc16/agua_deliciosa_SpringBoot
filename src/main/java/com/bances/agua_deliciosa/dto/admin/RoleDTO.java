package com.bances.agua_deliciosa.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleDTO {
    private Long id;

    @NotBlank(message = "El nombre del rol es requerido")
    private String name;
}
