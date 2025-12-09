package com.ObraSmart.GestionReparaciones.client;

import com.ObraSmart.GestionReparaciones.dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "gestion-login", url = "${gateway.url:http://localhost:8085}")
public interface UsuarioClient {

    @GetMapping("/users/{id}")
    UsuarioDTO obtenerUsuario(@PathVariable("id") Long id);
}
