package com.bances.agua_deliciosa.model;

public enum Gender {
    M("Masculino"),
    F("Femenino"),
    O("Otro");
    
    private final String displayName;
    
    Gender(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
} 