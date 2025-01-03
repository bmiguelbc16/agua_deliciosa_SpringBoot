package com.bances.agua_deliciosa.service.system;

import org.springframework.stereotype.Service;
import org.springframework.core.annotation.Order;
import jakarta.annotation.PostConstruct;
import com.bances.agua_deliciosa.db.seeders.Seeder;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@Order(2) // Se ejecuta después de DatabaseMigrationService
@RequiredArgsConstructor
public class DatabaseSeederService {

    private final List<Seeder> seeders;

    @PostConstruct
    public void init() {
        System.out.println("Iniciando el proceso de Seed...");
        executeSeeds();
    }

    private void executeSeeds() {
        // Ejecuta los seeders respetando sus dependencias
        seeders.stream()
                .sorted((s1, s2) -> compareDependencies(s1, s2)) // Ordena los seeders según sus dependencias
                .forEach(seeder -> {
                    if (!seeder.alreadySeeded()) {
                        try {
                            System.out.println("Ejecutando Seeder: " + seeder.getClass().getSimpleName());
                            // Asegura que el seed se ejecute con transacción
                            boolean success = seeder.seedWithTransaction(); // Usando el método que maneja la transacción
                            if (!success) {
                                System.err.println("Error al ejecutar el Seeder: " + seeder.getClass().getSimpleName());
                            }
                        } catch (Exception e) {
                            System.err.println("Error al ejecutar el Seeder: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Seeder ya ejecutado: " + seeder.getClass().getSimpleName());
                    }
                });
    }

    private int compareDependencies(Seeder s1, Seeder s2) {
        // Compara las dependencias entre seeders
        List<Class<? extends Seeder>> deps1 = s1.getDependencies();
        List<Class<? extends Seeder>> deps2 = s2.getDependencies();
        
        // Asegura que los seeders con dependencias se ejecuten primero
        if (deps1.contains(s2.getClass())) return 1;  // s1 depende de s2, s1 va después
        if (deps2.contains(s1.getClass())) return -1; // s2 depende de s1, s2 va después
        return 0; // No hay dependencia directa
    }
}
