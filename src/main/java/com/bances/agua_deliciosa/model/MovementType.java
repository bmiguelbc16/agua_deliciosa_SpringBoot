package com.bances.agua_deliciosa.model;

public enum MovementType {
    ENTRY("E", "Entrada", true),
    OUTPUT("S", "Salida", false),
    SALE("V", "Venta", false),
    PURCHASE("C", "Compra", true),
    NEUTRAL("N", "Neutro", false);

    private final String code;
    private final String description;
    private final boolean isPositive;

    MovementType(String code, String description, boolean isPositive) {
        this.code = code;
        this.description = description;
        this.isPositive = isPositive;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPositive() {
        return isPositive;
    }

    public static MovementType fromCode(String code) {
        for (MovementType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid movement type code: " + code);
    }
}
