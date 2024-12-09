package com.bances.agua_deliciosa.config.provider;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.lang.NonNull;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RouteServiceProvider implements WebMvcConfigurer {
    
    @Override
    public void addViewControllers(@NonNull ViewControllerRegistry registry) {
        log.info("Configurando rutas principales...");
        
        // Rutas públicas
        registry.addViewController("/").setViewName("welcome");
        registry.addViewController("/login").setViewName("auth/login");
        
        // Rutas de autenticación
        registry.addViewController("/password/reset").setViewName("auth/passwords/reset");
        registry.addViewController("/password/confirm").setViewName("auth/passwords/confirm");
        registry.addViewController("/password/email").setViewName("auth/passwords/email");
        
        // Rutas de dashboard por rol
        registry.addViewController("/dashboard").setViewName("dashboard");
        registry.addViewController("/admin/dashboard").setViewName("admin/dashboard");
        registry.addViewController("/cliente/dashboard").setViewName("cliente/dashboard");
        registry.addViewController("/gerente/dashboard").setViewName("gerente/dashboard");
        registry.addViewController("/vendedor/dashboard").setViewName("vendedor/dashboard");
        registry.addViewController("/repartidor/dashboard").setViewName("repartidor/dashboard");
    }
    
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/dist/**")
                .addResourceLocations("classpath:/static/dist/")
                .setCachePeriod(3600);
                
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/")
                .setCachePeriod(3600);
                
        registry.addResourceHandler("/plugins/**")
                .addResourceLocations("classpath:/static/plugins/")
                .setCachePeriod(3600);
    }
    
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .maxAge(3600);
    }
}