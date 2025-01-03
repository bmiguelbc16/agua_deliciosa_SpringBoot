package com.bances.agua_deliciosa.db.seeders;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

public interface Seeder {

    /**
     * Ejecuta el seeder para poblar la base de datos.
     * @return true si el seeder se ejecutó correctamente, false en caso contrario.
     */
    boolean seed();

    /**
     * Verifica si el seeder ya se ha ejecutado previamente.
     * @return true si el seeder ya ha sido ejecutado, false en caso contrario.
     */
    default boolean alreadySeeded() {
        return false; // Por defecto, no se ha ejecutado, puedes personalizar esta lógica.
    }

    /**
     * Método para obtener las dependencias de este seeder.
     * Un seeder puede depender de otros seeders.
     * @return una lista de seeders que deben ejecutarse antes.
     */
    default List<Class<? extends Seeder>> getDependencies() {
        return List.of(); // Sin dependencias por defecto.
    }

    /**
     * Método que maneja el proceso de seed con control de transacciones.
     * Utiliza Spring @Transactional para manejo de transacciones.
     * @return true si el proceso fue exitoso, false si hubo algún error.
     */
    @Transactional
    default boolean seedWithTransaction() {
        try {
            boolean result = seed();
            if (!result) {
                throw new RuntimeException("Error al ejecutar el seeder");
            }
            return true;
        } catch (RuntimeException e) {
            handleError(e);
            return false;
        }
    }

    default void handleError(Exception e) {
        System.err.println("Error al ejecutar el seeder: " + e.getMessage());
    }
}
