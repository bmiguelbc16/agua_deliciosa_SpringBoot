package com.bances.agua_deliciosa.dto.admin;

import com.bances.agua_deliciosa.dto.base.BaseUserDTO;
import com.bances.agua_deliciosa.service.core.RoleService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
public class ClientDTO extends BaseUserDTO {
    
    @Autowired
    private RoleService roleService;
    
    public ClientDTO() {
        this.setActive(true);
        this.setUserableType("Client");
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        // Buscar el ID del rol "Cliente" y asignarlo
        if (roleService != null) {
            this.setRoleId(roleService.findByName("Cliente").getId());
        }
    }
    
    @Override
    public String toString() {
        return "ClientDTO{" +
            "id=" + getId() +
            ", name='" + getName() + '\'' +
            ", lastName='" + getLastName() + '\'' +
            ", email='" + getEmail() + '\'' +
            ", documentNumber='" + getDocumentNumber() + '\'' +
            ", phoneNumber='" + getPhoneNumber() + '\'' +
            ", birthDate=" + getBirthDate() +
            ", gender='" + getGender() + '\'' +
            ", active=" + getActive() +
            ", roleId=" + getRoleId() +
            '}';
    }
}
