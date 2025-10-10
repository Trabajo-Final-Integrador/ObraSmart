package com.ObraSmart.GestionEquipos.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TipoEquipoDTO {

    private Long id;

    @NotBlank (message = "El nombre no puede estar vacío")
    private String nombre;
}
