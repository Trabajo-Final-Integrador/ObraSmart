package com.obrasmart.gestionusuario.dto;

import com.obrasmart.gestionusuario.entity.Role;
import com.obrasmart.gestionusuario.entity.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO de entrada para crear usuarios.
 */
@Data
public class UsuarioCreateRequest {

    @NotBlank
    @Size(max = 80)
    private String username;

    @NotBlank
    @Email
    @Size(max = 120)
    private String email;

    @Size(max = 80)
    private String nombre;

    @Size(max = 80)
    private String apellido;

    @Size(max = 30)
    private String telefono;

    @NotNull
    private Role role;

    @NotNull
    private Status status;
}
