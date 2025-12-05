package com.ObraSmart.GestionReparaciones.dto;


import com.ObraSmart.GestionReparaciones.entity.EstadoReparacion;
import com.ObraSmart.GestionReparaciones.entity.TipoMantenimiento;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReparacionRequestDto {

    @NotNull
    private Long equipoId;

    @NotNull
    private TipoMantenimiento tipoMantenimiento;

    // Estado inicial (opcional). Si viene null → CREADA
    private EstadoReparacion estadoReparacion;

    private String descripcion;

    // Responsable
    @NotNull
    private Long responsableId;

    // opcional: se puede enviar para mostrar
    private String responsableNombreCompleto;

    // Geolocalización
    private String direccion;
    private Double lat;
    private Double lon;
}
