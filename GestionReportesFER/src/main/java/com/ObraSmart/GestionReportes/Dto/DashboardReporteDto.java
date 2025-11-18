package com.ObraSmart.GestionReportes.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardReporteDto {

    private Long id;
    private LocalDateTime fechaGeneracion;

    private Long totalReparaciones;
    private Long totalReparacionesAbiertas;
    private Long totalReparacionesFinalizadas;

    private Long totalEquipos;
    private Long totalRepuestos;
    private Long totalMovimientos;

    private Long repuestosCriticos;
}
