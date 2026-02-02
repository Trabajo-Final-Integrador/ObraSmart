package com.ObraSmart.GestionLogistica.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TrasladoRequestDto(
        @NotNull Long equipoId,
        Long origenObradorId,
        @NotNull Long destinoObradorId,
        LocalDateTime programadoPara,
        String notas
) {}
