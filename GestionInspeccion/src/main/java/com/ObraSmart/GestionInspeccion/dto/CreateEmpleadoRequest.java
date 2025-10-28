package com.ObraSmart.GestionInspeccion.dto;

import com.ObraSmart.GestionInspeccion.entity.Role;
import com.ObraSmart.GestionInspeccion.entity.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmpleadoRequest {
    @NotBlank private String nombre;
    @NotBlank private String apellido;
    @NotBlank private String dni;
    @Email
    @NotBlank private String email;
    @NotBlank private String direccion;
    @NotBlank
    private String telefono;
    @NotNull
    private Role role;
    @NotBlank
    private Status status;
}
