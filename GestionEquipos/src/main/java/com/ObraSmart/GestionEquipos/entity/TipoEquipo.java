package com.ObraSmart.GestionEquipos.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tipo_equipo")
public class TipoEquipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(name = "imagen_url")
    private String imagenURL;

    /**
     * Prefijo usado para generar el código interno del equipo.
     * - Solo letras A–Z
     * - Entre 1 y 4 caracteres (RET, EXC, CAM, CAT, VOLV)
     */
    @Column(name = "prefijo", nullable = false, length = 4)
    private String prefijo;
}
