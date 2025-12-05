package com.ObraSmart.GestionReportes.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "dashboard_snapshot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaGeneracion;

    private Long totalReparaciones;
    private Long totalReparacionesAbiertas;
    private Long totalReparacionesFinalizadas;

    private Long totalEquipos;
    private Long totalRepuestos;
    private Long totalMovimientos;

    private Long repuestosCriticos;
}
