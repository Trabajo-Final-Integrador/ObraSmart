package com.ObraSmart.GestionEquipos.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Modelo {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_marca")
    private Marca marca;

}
