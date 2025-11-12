package com.ObraSmart.GestionGeolocalizacion.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad para guardar la ubicación de un equipo.
 * Puede usarse para cache local de coordenadas.
 */
@Entity
@Table(name = "equipo_ubicacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipoUbicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long equipoId;

    @Column(nullable = false)
    private String nombre;

    private double lat;
    private double lon;
    private String estado;
}
