package com.bances.agua_deliciosa.dto.admin;

import com.bances.agua_deliciosa.dto.base.BaseUserDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDTO extends BaseUserDTO {
    private String roles;  // Necesitamos este campo para el rol del empleado
    
    public EmployeeDTO() {
        this.setActive(true);
        this.setUserableType("Employee");
    }
    
    @Override
    public String toString() {
        return "EmployeeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + '\'' +
            ", lastName='" + getLastName() + '\'' +
            ", email='" + getEmail() + '\'' +
            ", documentNumber='" + getDocumentNumber() + '\'' +
            ", phoneNumber='" + getPhoneNumber() + '\'' +
            ", roles='" + getRoles() + '\'' +
            ", birthDate=" + getBirthDate() +
            ", active=" + getActive() +
            '}';
    }
}
