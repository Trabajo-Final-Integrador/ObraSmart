package com.ObraSmart.GestionInspeccion.dto;

import com.ObraSmart.GestionInspeccion.entity.Role;
import com.ObraSmart.GestionInspeccion.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private Role role;
    private Status status;
}
