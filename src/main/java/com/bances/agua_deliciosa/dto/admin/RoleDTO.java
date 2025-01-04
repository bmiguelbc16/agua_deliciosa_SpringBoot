package com.bances.agua_deliciosa.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.Set;

@Data
public class RoleDTO {
    
    private Long id;
    
    @NotBlank(message = "El nombre es requerido")
    private String name;
    
    private String description;
    
    private boolean active = true;
    
    private Set<Long> permissionIds;
}
