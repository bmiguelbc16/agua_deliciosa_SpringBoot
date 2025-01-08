package com.bances.agua_deliciosa.db.seeders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final List<Seeder> seeders;

    @Override
    @Transactional(propagation = Propagation.NEVER)
    public void run(String... args) {
        log.info("Verificando si es necesario ejecutar seeders...");
        
        boolean anyErrors = false;
        
        for (Seeder seeder : seeders) {
            try {
                if (seeder.shouldSeed()) {
                    log.info("Ejecutando seeder: {}", seeder.getClass().getSimpleName());
                    seeder.seedWithTransaction();
                    log.info("Seeder {} completado exitosamente", seeder.getClass().getSimpleName());
                } else {
                    log.info("Saltando seeder: {} (no es necesario)", seeder.getClass().getSimpleName());
                }
            } catch (Exception e) {
                anyErrors = true;
                log.error("Error al ejecutar seeder {}: {}", seeder.getClass().getSimpleName(), e.getMessage(), e);
            }
        }
        
        if (anyErrors) {
            throw new RuntimeException("Uno o más seeders fallaron. Revise los logs para más detalles.");
        }
        
        log.info("Proceso de seeding completado.");
    }
}
