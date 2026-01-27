package com.ObraSmart.GestionLogistica.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipoSimpleDto {
    private Long id;
    private String nombre;
    private String codigoInterno;
    private Double latitud;
    private Double longitud;
    private String ubicacionActual;
}
