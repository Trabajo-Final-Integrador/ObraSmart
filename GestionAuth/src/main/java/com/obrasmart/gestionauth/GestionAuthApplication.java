package com.obrasmart.gestionauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada del microservicio GestionAuth.
 * Carga el contexto de Spring Boot y todos los beans (Security, JPA, etc.).
 * Este servicio se dedica exclusivamente a autenticación basada en sesión (JSESSIONID).
 */
@SpringBootApplication
public class GestionAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(GestionAuthApplication.class, args);
    }
}
