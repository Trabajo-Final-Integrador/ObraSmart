package com.obrasmart.gestionusuario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada del microservicio GestionUsuario.
 * Se limita al CRUD de usuarios extendidos, autenticado vía sesión (JSESSIONID) gestionada por GestionAuth.
 */
@SpringBootApplication
public class GestionUsuarioApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionUsuarioApplication.class, args);
    }
}
