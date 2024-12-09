package com.bances.agua_deliciosa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.PostConstruct;

import com.bances.agua_deliciosa.service.DatabaseSeederService;

@SpringBootApplication
@ComponentScan({"com.bances.agua_deliciosa.*"})
public class AguaDeliciosaApplication {

	@Autowired
	private DatabaseSeederService seederService;

	public static void main(String[] args) {
		SpringApplication.run(AguaDeliciosaApplication.class, args);
	}

	@PostConstruct
    public void init() {
        seederService.seedDatabase();
    }
}
	