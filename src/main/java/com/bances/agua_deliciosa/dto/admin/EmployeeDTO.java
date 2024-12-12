package com.bances.agua_deliciosa.dto.admin;

import com.bances.agua_deliciosa.dto.base.BaseUserDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDTO extends BaseUserDTO {
    
    @NotBlank(message = "El rol es requerido")
    private String role;
    
    @Override
    public String toString() {
        return "EmployeeDTO{" +
            "name='" + getName() + '\'' +
            ", lastName='" + getLastName() + '\'' +
            ", email='" + getEmail() + '\'' +
            ", documentNumber='" + getDocumentNumber() + '\'' +
            ", phoneNumber='" + getPhoneNumber() + '\'' +
            ", role='" + role + '\'' +
            '}';
    }
} 