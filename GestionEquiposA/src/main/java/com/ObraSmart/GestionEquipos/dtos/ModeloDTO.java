package com.ObraSmart.GestionEquipos.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModeloDTO {

    private Long id;

    @NotBlank (message = "El nombre no puede estar vacío")
    private String nombre;

    @NotNull (message = "La marca no puede ser nula")
    private MarcaDTO marca;


}
