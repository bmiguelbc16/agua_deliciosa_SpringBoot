package com.bances.agua_deliciosa.config.provider;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class EventServiceProvider {

    // Ya no es necesario el @Bean si no se va a devolver un objeto
    public void eventConfiguration() {
        log.info("Registrando eventos de la aplicación...");
        // Lógica de configuración, sin devolver nada
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("Aplicación iniciada y lista");
    }
}
