package com.ObraSmart.GestionReparaciones.dto;

import com.ObraSmart.GestionReparaciones.entity.EstadoReparacion;
import com.ObraSmart.GestionReparaciones.entity.TipoMantenimiento;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReparacionResponseDto {

    private Long id;

    // Equipo
    private Long equipoId;
    private String equipoDescripcion; // opcional (puede venir del micro Equipos)

    // Mantenimiento
    private TipoMantenimiento tipoMantenimiento;
    private EstadoReparacion estadoReparacion;
    private String descripcion;

    // Responsable
    private Long responsableId;              // 🔹 el ID que pediste
    private String responsableNombreCompleto; // 🔹 el nombre y apellido de ese ID

    // Geolocalización
    private String direccion;
    private Double lat;
    private Double lon;

    // Auditoría
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private LocalDateTime fechaUltimaActualizacion;
    private Long usuarioUltimaActualizacionId;

    // Historial
    private List<ReparacionEstadoHistorialDto> historialEstados;
    private String equipoNombre;
    private String equipoCodigoInterno;


}
