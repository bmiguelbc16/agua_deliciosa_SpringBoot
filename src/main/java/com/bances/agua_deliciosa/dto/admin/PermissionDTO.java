package com.bances.agua_deliciosa.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PermissionDTO {
    
    private Long id;
    
    @NotBlank(message = "El nombre es requerido")
    private String name;
    
    @NotBlank(message = "La descripción es requerida")
    private String description;
    
    private boolean active = true;
}
