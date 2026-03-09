package com.git.gestion_turnos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GestionTurnosApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionTurnosApplication.class, args);
	}

}
