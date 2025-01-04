package com.bances.agua_deliciosa.db.seeders;

import java.util.Collections;
import java.util.Set;

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
    
    default void seedWithTransaction() {
        seed();
    }
    
    default Set<Class<? extends Seeder>> getDependencies() {
        return Collections.emptySet();
    }
}
