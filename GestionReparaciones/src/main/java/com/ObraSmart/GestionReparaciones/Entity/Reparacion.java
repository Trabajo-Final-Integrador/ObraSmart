package com.ObraSmart.GestionReparaciones.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "reparaciones")
@Data
public class Reparacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================== EQUIPO ==================
    @Column(name = "equipo_id", nullable = false)
    private Long equipoId;   // viene del micro de Equipos

    // ================== MANTENIMIENTO ==================
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_mantenimiento", nullable = false)
    private TipoMantenimiento tipoMantenimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_reparacion", nullable = false)
    private EstadoReparacion estadoReparacion = EstadoReparacion.CREADA;

    @Column(name = "descripcion", length = 2000)
    private String descripcion;

    // ================== RESPONSABLE ==================
    @Column(name = "responsable_id", nullable = false)
    private Long responsableId;   // ID de usuario (micro de usuarios)

    @Column(name = "responsable_nombre_completo")
    private String responsableNombreCompleto; // nombre + apellido (para mostrar rápido)

    // ================== GEOLOCALIZACIÓN ==================
    @Column(name = "direccion")
    private String direccion;

    @Column(name = "latitud")
    private Double lat;

    @Column(name = "longitud")
    private Double lon;

    // ================== AUDITORÍA ==================
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(name = "fecha_ultima_actualizacion")
    private LocalDateTime fechaUltimaActualizacion = LocalDateTime.now();

    @Column(name = "usuario_ultima_actualizacion")
    private Long usuarioUltimaActualizacionId;
}
