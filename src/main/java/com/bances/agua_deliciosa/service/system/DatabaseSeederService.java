package com.bances.agua_deliciosa.service.system;

import com.bances.agua_deliciosa.db.seeders.Seeder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DatabaseSeederService {
    
    private final List<Seeder> seeders;
    private final Set<Class<? extends Seeder>> seededClasses = new HashSet<>();
    private final Set<Class<? extends Seeder>> processingClasses = new HashSet<>();
    
    @Transactional
    public void seedDatabase() {
        seeders.forEach(this::seedWithDependencies);
    }
    
    private void seedWithDependencies(Seeder seeder) {
        Class<? extends Seeder> seederClass = seeder.getClass();
        
        // Si ya fue procesado, retornar
        if (seededClasses.contains(seederClass)) {
            return;
        }
        
        // Detectar dependencias cíclicas
        if (!processingClasses.add(seederClass)) {
            throw new RuntimeException("Dependencia cíclica detectada al procesar: " + seederClass.getSimpleName());
        }
        
        try {
            // Si no necesita ejecutarse, marcarlo como procesado y retornar
            if (!seeder.shouldSeed()) {
                seededClasses.add(seederClass);
                return;
            }
            
            // Procesar dependencias primero
            getDependencies(seeder).forEach(dependencyClass -> {
                Seeder dependency = findSeederByClass(dependencyClass);
                if (dependency != null) {
                    seedWithDependencies(dependency);
                }
            });
            
            // Ejecutar el seeder actual
            seeder.seed();
            seededClasses.add(seederClass);
            
        } finally {
            processingClasses.remove(seederClass);
        }
    }
    
    private Seeder findSeederByClass(Class<? extends Seeder> seederClass) {
        return seeders.stream()
                .filter(s -> seederClass.isInstance(s))
                .findFirst()
                .orElse(null);
    }
    
    private Set<Class<? extends Seeder>> getDependencies(Seeder seeder) {
        // Por defecto, no hay dependencias
        return Collections.emptySet();
    }
}
