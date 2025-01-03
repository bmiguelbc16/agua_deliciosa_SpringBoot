package com.bances.agua_deliciosa.dto.base;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public abstract class BaseDTO implements Serializable {
    private Long id;
    private boolean active = true;

    // Métodos comunes para todos los DTOs
    public boolean isNew() {
        return id == null;
    }
}
