package com.bances.agua_deliciosa.exception;

// Excepción personalizada para manejar contraseña incorrecta
public class InvalidPasswordException extends RuntimeException {
    
    // Constructor con mensaje personalizado
    public InvalidPasswordException(String message) {
        super(message);
    }
}
