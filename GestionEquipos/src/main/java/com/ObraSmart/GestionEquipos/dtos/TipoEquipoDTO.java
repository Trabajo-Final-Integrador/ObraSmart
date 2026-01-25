package com.ObraSmart.GestionEquipos.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoEquipoDTO {

    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    /**
     * Prefijo del código interno del tipo de equipo.
     * Debe tener entre 1 y 4 letras mayúsculas A–Z.
     */
    @NotBlank(message = "El prefijo no puede estar vacío")
    @Pattern(
            regexp = "^[A-Z]{1,4}$",
            message = "El prefijo debe tener entre 1 y 4 letras mayúsculas (A–Z)"
    )
    private String prefijo;

    private String descripcion;

    private String imagenURL;
}
