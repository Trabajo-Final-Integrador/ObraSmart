package com.ObraSmart.GestionReparaciones.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "reparaciones_historial_estado")
@Data
public class ReparacionEstadoHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cada cambio de estado pertenece a una reparación
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reparacion_id", nullable = false)
    private Reparacion reparacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_anterior")
    private EstadoReparacion estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_nuevo", nullable = false)
    private EstadoReparacion estadoNuevo;

    @Column(name = "fecha_cambio", nullable = false)
    private LocalDateTime fechaCambio = LocalDateTime.now();

    @Column(name = "usuario_responsable_id", nullable = false)
    private Long usuarioResponsableId;   // el id del usuario que hizo el cambio

    @Column(name = "comentario")
    private String comentario;
}
