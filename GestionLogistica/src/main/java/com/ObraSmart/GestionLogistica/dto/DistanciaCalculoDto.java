package com.ObraSmart.GestionLogistica.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DistanciaCalculoDto {

    private Long equipoId;
    private String equipoNombre;
    private Double equipoLatitud;
    private Double equipoLongitud;

    private Long obradorId;
    private String obradorNombre;
    private Double obradorLatitud;
    private Double obradorLongitud;

    private Double distanciaKm;
    private String mensaje;
}