package com.ObraSmart.GestionReportes.Dto.Externos;

public record RepuestoDto(
        Long id,
        String codigo,
        String nombre,
        Long idCategoria,
        Integer stock,
        Integer stockMinimo
) {}
