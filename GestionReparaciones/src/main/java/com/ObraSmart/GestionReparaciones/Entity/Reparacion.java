package com.ObraSmart.GestionReparaciones.Entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad principal: representa un registro de mantenimiento/reparación.
 * Notar: no se crea FK a tabla equipos (microservicio separado). Solo se guarda equipoId.
 */
@Entity
@Table(name = "reparaciones")
public class Reparacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // id del equipo en el microservicio GestionEquipos
    @Column(nullable = false)
    private Long equipoId;

    @Column(length = 1000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private EstadoReparacion estado;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    // Geolocalización opcional
    private Double lat;
    private Double lon;

    private String direccion; // dirección textual opcional

    // Getters y setters (puedes reemplazar por Lombok @Data si lo usas)
    public Reparacion() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEquipoId() { return equipoId; }
    public void setEquipoId(Long equipoId) { this.equipoId = equipoId; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public EstadoReparacion getEstado() { return estado; }
    public void setEstado(EstadoReparacion estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }

    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }

    public Double getLon() { return lon; }
    public void setLon(Double lon) { this.lon = lon; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}
