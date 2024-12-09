package com.bances.agua_deliciosa.config.provider;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class EventServiceProvider {
    
    @Bean
    public void eventConfiguration() {
        log.info("Registrando eventos de la aplicación...");
        // Configuración de eventos
    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("Aplicación iniciada y lista");
    }
}