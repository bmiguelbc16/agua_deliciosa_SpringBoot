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
            // Ejecutar los seeders en orden según la anotación @Order
            System.out.println("Verificando si es necesario ejecutar seeders...");
            seeders.forEach(seeder -> {
                try {
                    if (seeder.shouldSeed()) {
                        System.out.println("Ejecutando " + seeder.getClass().getSimpleName() + "...");
                        seeder.seed();
                        System.out.println(seeder.getClass().getSimpleName() + " ejecutado exitosamente");
                    } else {
                        System.out.println("Saltando " + seeder.getClass().getSimpleName() + " - Ya existen datos");
                    }
                } catch (Exception e) {
                    System.err.println("Error al ejecutar seeder " + seeder.getClass().getSimpleName() + ": " + e.getMessage());
                    throw e;
                }
            });
            System.out.println("Proceso de seeding completado");
        } catch (Exception e) {
            System.err.println("Error en DatabaseSeeder: " + e.getMessage());
            throw e;
        }
    }
}
