package com.bances.agua_deliciosa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AguaDeliciosaApplication {
    public static void main(String[] args) {
        SpringApplication.run(AguaDeliciosaApplication.class, args);
    }
}