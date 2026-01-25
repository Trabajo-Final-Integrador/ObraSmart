package com.ObraSmart.GestionObrador.dto;

import jakarta.validation.constraints.NotNull;

public record AsignarEquipoDto(
        @NotNull Long equipoId
) {}
