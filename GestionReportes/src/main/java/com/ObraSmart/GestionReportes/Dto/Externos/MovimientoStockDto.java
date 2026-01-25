package com.ObraSmart.GestionReportes.dto.externos;

import java.time.Instant;

public record MovimientoStockDto(
        Long id,
        Long idRepuesto,
        String tipo,      // ENTRADA / SALIDA
        Integer cantidad,
        String observacion,
        Instant fecha
) {}
