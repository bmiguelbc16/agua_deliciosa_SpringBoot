package com.bances.agua_deliciosa.db.seeders;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final List<Seeder> seeders;

    @Override
    @Transactional
    public void run(String... args) {
        try {
            System.out.println("Verificando si es necesario ejecutar seeders...");
            
            for (Seeder seeder : seeders) {
                if (seeder.shouldSeed()) {
                    System.out.println("Ejecutando seeder: " + seeder.getClass().getSimpleName());
                    seeder.seedWithTransaction();
                } else {
                    System.out.println("Saltando seeder: " + seeder.getClass().getSimpleName() + " (no es necesario)");
                }
            }
            
            System.out.println("Seeders completados exitosamente.");
        } catch (Exception e) {
            System.err.println("Error al ejecutar los seeders: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
