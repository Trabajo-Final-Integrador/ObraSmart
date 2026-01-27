package com.ObraSmart.GestionLogistica.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObradorSimpleDto {
    private Long id;
    private String nombre;
    private String ubicacion;
    private Double latitud;
    private Double longitud;
}