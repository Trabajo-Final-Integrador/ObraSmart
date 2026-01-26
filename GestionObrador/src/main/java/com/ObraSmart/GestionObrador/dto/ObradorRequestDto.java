package com.ObraSmart.GestionObrador.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record ObradorRequestDto(
        @NotBlank String nombre,
        @Size(max = 255) String ubicacion,
        Double latitud,
        Double longitud,
        Long supervisorUserId,
        Set<Long> equipoIds
) {}
