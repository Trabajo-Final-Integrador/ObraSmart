package com.ObraSmart.GestionReportes.Dto.Externos;

import java.time.LocalDateTime;

public record ReparacionResponseDto(
        Long id,
        Long equipoId,
        String descripcion,
        String estado,
        Double lat,
        Double lon,
        String direccion,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin
) {}
