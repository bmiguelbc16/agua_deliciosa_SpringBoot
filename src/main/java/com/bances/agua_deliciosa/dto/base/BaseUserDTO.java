package com.bances.agua_deliciosa.dto.base;

import java.time.LocalDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;

@Getter
@Setter
public class BaseUserDTO implements InitializingBean {
    private Long id;
    
    @NotBlank(message = "El nombre es requerido")
    private String name;
    
    @NotBlank(message = "El apellido es requerido")
    private String lastName;
    
    @Email(message = "Debe ingresar un email válido")
    @NotBlank(message = "El email es requerido")
    private String email;
    
    @NotBlank(message = "El número de documento es requerido")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
    private String documentNumber;
    
    @NotBlank(message = "El teléfono es requerido")
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener 9 dígitos")
    private String phoneNumber;
    
    // Validación condicional para la contraseña
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres", groups = OnCreate.class)
    private String password;
    
    private Boolean active;
    
    @NotNull(message = "La fecha de nacimiento es requerida")
    @Past(message = "La fecha debe ser en el pasado")
    private LocalDate birthDate;
    
    @NotBlank(message = "El género es requerido")
    private String gender;
    
    // Para la relación uno a uno con role
    @NotNull(message = "Debe seleccionar un rol")
    private Long roleId;
    
    private String roleName;
    
    private String userableType;
    private Long userableId;
    
    // Interfaz para validación en creación
    public interface OnCreate {}

    @Override
    public void afterPropertiesSet() throws Exception {
        // Este método será sobreescrito por las clases hijas si necesitan inicializar algo
    }
}
