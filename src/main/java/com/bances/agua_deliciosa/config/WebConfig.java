package com.bances.agua_deliciosa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(0);
        
        registry.addResourceHandler("/plugins/**")
                .addResourceLocations("classpath:/static/plugins/")
                .setCachePeriod(0);
        
        registry.addResourceHandler("/dist/**")
                .addResourceLocations("classpath:/static/dist/")
                .setCachePeriod(0);
    }
} 