package com.ObraSmart.GestionReportes.dto.externos;

import java.time.LocalDateTime;

public record ReparacionResponseDto(
        Long id,
        Long equipoId,
        String descripcion,
        String estadoReparacion,
        String tipoMantenimiento,
        Double lat,
        Double lon,
        String direccion,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin


) {}
