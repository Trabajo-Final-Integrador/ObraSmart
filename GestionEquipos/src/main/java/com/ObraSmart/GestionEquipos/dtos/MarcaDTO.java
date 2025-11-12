package com.ObraSmart.GestionEquipos.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarcaDTO {

    private Long id;

    @NotBlank (message = "El nombre no puede estar vacío")
    private String nombre;
}
