package com.bances.agua_deliciosa.model;

public enum VerificationType {
    EMAIL("E", "Email"),
    PHONE("P", "Teléfono"),
    TWO_FACTOR("2FA", "Autenticación de dos factores");

    private final String code;
    private final String description;

    VerificationType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static VerificationType fromCode(String code) {
        for (VerificationType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid verification type code: " + code);
    }
}
