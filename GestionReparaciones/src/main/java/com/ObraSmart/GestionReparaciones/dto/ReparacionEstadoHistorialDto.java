package com.ObraSmart.GestionReparaciones.dto;


import com.ObraSmart.GestionReparaciones.entity.EstadoReparacion;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReparacionEstadoHistorialDto {

    private Long id;
    private EstadoReparacion estadoAnterior;
    private EstadoReparacion estadoNuevo;
    private LocalDateTime fechaCambio;
    private Long usuarioResponsableId;
    private String comentario;
}
