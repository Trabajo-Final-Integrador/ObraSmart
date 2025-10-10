package com.ObraSmart.GestionEquipos.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModeloDTO {

    private Long id;

    @NotBlank (message = "El nombre no puede estar vacío")
    private String nombre;

    @NotNull (message = "La marca no puede ser nula")
    private MarcaDTO marca;

    @NotNull (message = "El tipo de equipo no puede ser nulo")
    private TipoEquipoDTO tipoEquipo;
}
