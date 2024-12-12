package com.bances.agua_deliciosa.db.seeders;

@FunctionalInterface
public interface Seeder {
    /**
     * Ejecuta el seeder para poblar la base de datos
     */
    void seed();
    
    /**
     * Método por defecto para verificar si el seeder ya se ejecutó
     */
    default boolean alreadySeeded() {
        return false;
    }
    
    /**
     * Método por defecto para obtener la dependencia del seeder
     */
    default Class<? extends Seeder> getDependency() {
        return null;
    }
} 