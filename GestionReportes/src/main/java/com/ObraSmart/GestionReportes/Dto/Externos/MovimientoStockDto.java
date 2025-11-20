package com.ObraSmart.GestionReportes.Dto.Externos;

public record MovimientoStockDto(
        Long id,
        Long idRepuesto,
        String tipo,      // ENTRADA / SALIDA
        Integer cantidad,
        String observacion
) {}
