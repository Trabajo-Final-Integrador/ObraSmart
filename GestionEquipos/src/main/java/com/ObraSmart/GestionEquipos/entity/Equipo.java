package com.ObraSmart.GestionEquipos.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Equipo {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_tipo_equipo", nullable = false)
    private TipoEquipo tipoEquipo;

    @ManyToOne
    @JoinColumn(name = "id_marca", nullable = false)
    private Marca marca;

    @ManyToOne
    @JoinColumn(name = "id_modelo", nullable = false)
    private Modelo modelo;

    @Column(name = "numero_serie", nullable = false, unique = true)
    private String numeroSerie;

    @Column(name = "anio_fabricacion", nullable = false)
    private Integer anioFabricacion;

    @Column(name = "numero_motor", nullable = false)
    private Double potenciaHp;

    @Enumerated(EnumType.STRING)
    @Column(name = "combustible", nullable = false)
    private Combustible combustible;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "estado_operativo", nullable = false)
    private Estado_Operativo estadoOperativo;

    @Column(name = "kilometraje_horas_uso", nullable = false)
    private Double kilometrajeHorasUso;

    @Column(name = "fecha_ultimo_mantenimiento", nullable = false)
    private LocalDate fechaUltimoMantenimiento;

    @Column(name = "proximo_mantenimiento", nullable = false)
    private LocalDate proximoMantenimiento;

    @Column(name = "responsable_mantenimiento", nullable = false)
    private String responsableMantenimiento;

    @Column(name = "numero_patente", nullable = true, unique = true)
    private String numeroPatente;

    @Column(name = "seguro_vigente", nullable = false)
    private Boolean seguroVigente;

    @Column(name = "fecha_vencimiento_seguro", nullable = true)
    private LocalDate fechaVencimientoSeguro;

    @Column(name = "ubicacion_actual", nullable = false)
    private String ubicacionActual;

    @Column(name = "activo", nullable = false)
    private Boolean activo;


}
