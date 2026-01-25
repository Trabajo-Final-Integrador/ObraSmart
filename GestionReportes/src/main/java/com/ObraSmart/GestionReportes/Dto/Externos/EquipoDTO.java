package com.ObraSmart.GestionReportes.dto.externos;

public record EquipoDTO(
        Long id,
        String codigoInterno,
        String nombre,
        Long idTipoEquipo,
        Long idMarca,
        Long idModelo,
        String combustible,
        String estadoOperativo,
        Double kilometrajeHorasUso
) {}
