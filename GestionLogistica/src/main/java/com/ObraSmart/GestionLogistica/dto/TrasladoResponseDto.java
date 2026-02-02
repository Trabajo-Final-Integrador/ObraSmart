package com.ObraSmart.GestionLogistica.dto;

import com.ObraSmart.GestionLogistica.entity.TrasladoEquipo;

import java.time.LocalDateTime;

public record TrasladoResponseDto(
        Long id,
        Long equipoId,
        Long origenObradorId,
        String origenUbicacion,
        Long destinoObradorId,
        LocalDateTime programadoPara,
        LocalDateTime fechaCreacion,
        TrasladoEquipo.EstadoTraslado estado,
        String notas
) {}
