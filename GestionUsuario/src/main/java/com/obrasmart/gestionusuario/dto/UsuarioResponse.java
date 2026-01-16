package com.obrasmart.gestionusuario.dto;

import com.obrasmart.gestionusuario.entity.Role;
import com.obrasmart.gestionusuario.entity.Status;
import lombok.Builder;
import lombok.Value;

/**
 * DTO de salida hacia el frontend.
 */
@Value
@Builder
public class UsuarioResponse {
    Long id;
    String username;
    String email;
    String nombre;
    String apellido;
    String telefono;
    Role role;
    Status status;
    boolean tieneLicenciaFrente;
    boolean tieneLicenciaDorso;
}
