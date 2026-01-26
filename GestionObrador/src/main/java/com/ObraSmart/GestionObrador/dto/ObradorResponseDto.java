package com.ObraSmart.GestionObrador.dto;

import java.util.Set;

public record ObradorResponseDto(
        Long id,
        String nombre,
        String ubicacion,
        Double latitud,
        Double longitud,
        Long supervisorUserId,
        Set<Long> equipoIds
) {}
