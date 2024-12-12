package com.bances.agua_deliciosa.dto.admin;

import com.bances.agua_deliciosa.dto.base.BaseUserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDTO extends BaseUserDTO {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
    
    @Override
    public String toString() {
        return "ProfileDTO{" +
            "name='" + getName() + '\'' +
            ", lastName='" + getLastName() + '\'' +
            ", email='" + getEmail() + '\'' +
            ", phoneNumber='" + getPhoneNumber() + '\'' +
            '}';
    }
}