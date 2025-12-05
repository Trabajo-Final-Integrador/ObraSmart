package com.ObraSmart.GestionReportes.dto.externos;

public record MovimientoStockDto(
        Long id,
        Long idRepuesto,
        String tipo,      // ENTRADA / SALIDA
        Integer cantidad,
        String observacion
) {}
