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
        executeSeeds();
    }
    
    private void executeSeeds() {
        seeders.forEach(seeder -> {
            if (!seeder.alreadySeeded()) {
                seeder.seed();
            }
        });
    }
}