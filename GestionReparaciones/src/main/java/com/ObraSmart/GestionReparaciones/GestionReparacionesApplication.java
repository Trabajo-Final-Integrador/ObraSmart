package com.ObraSmart.GestionReparaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GestionReparacionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionReparacionesApplication.class, args);
	}

}
