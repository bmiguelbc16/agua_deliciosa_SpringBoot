package com.bances.agua_deliciosa.db.seeders;

import java.util.Collections;
import java.util.Set;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface Seeder {
    /**
     * Verifica si el seeder debe ejecutarse
     * @return true si el seeder debe ejecutarse, false si no
     */
    boolean shouldSeed();

    /**
     * Ejecuta el seeder
     */
    void seed();
    
    /**
     * Ejecuta el seeder en una transacción independiente
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    default void seedWithTransaction() {
        try {
            seed();
        } catch (Exception e) {
            throw new RuntimeException("Error ejecutando seeder " + this.getClass().getSimpleName() + ": " + e.getMessage(), e);
        }
    }
    
    default Set<Class<? extends Seeder>> getDependencies() {
        return Collections.emptySet();
    }
}
