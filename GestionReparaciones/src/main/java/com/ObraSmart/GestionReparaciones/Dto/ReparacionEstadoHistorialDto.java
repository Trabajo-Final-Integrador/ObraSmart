package com.ObraSmart.GestionReparaciones.Dto;


import com.ObraSmart.GestionReparaciones.Entity.EstadoReparacion;
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
