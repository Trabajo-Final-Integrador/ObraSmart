package com.ObraSmart.GestionInspeccion.dto;

import com.ObraSmart.GestionInspeccion.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmpleadoRequest {
    private String direccion;
    private String telefono;
    private Status status;
}
