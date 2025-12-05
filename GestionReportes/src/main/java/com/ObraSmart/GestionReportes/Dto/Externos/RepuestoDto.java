package com.ObraSmart.GestionReportes.dto.externos;

public record RepuestoDto(
        Long id,
        String codigo,
        String nombre,
        Long idCategoria,
        Integer stock,
        Integer stockMinimo
) {}
