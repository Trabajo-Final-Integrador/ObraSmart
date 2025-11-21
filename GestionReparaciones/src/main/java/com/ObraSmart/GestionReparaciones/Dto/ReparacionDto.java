package com.ObraSmart.GestionReparaciones.Dto;



import jakarta.validation.constraints.NotNull;//debe agregar su dependencia en pom
import java.time.LocalDateTime;

/**
 * DTO recibido por POST. Validamos equipoId como obligatorio.
 */
public class ReparacionDto {
    private Long id;

    @NotNull(message = "equipoId es obligatorio")
    private Long equipoId;

    private String descripcion;
    private String direccion;
    private Double lat;
    private Double lon;
    private String estado; // opcional: string del enum
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEquipoId() { return equipoId; }
    public void setEquipoId(Long equipoId) { this.equipoId = equipoId; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }
    public Double getLon() { return lon; }
    public void setLon(Double lon) { this.lon = lon; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }
}
