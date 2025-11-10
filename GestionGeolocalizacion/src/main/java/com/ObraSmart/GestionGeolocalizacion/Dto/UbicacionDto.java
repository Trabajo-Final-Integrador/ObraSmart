package com.ObraSmart.GestionGeolocalizacion.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) para representar ubicaciones en las operaciones
 * entre la API REST, servicios y microservicios relacionados con equipos o reparaciones.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UbicacionDto {

    private Long id;

    /** Referencia externa (ID del equipo o reparación) */
    private String referencia;

    /** Tipo de la entidad asociada: "EQUIPO" o "REPARACION" */
    private String tipo;

    /** Dirección legible o texto descriptivo (opcional) */
    private String direccion;

    /** Latitud geográfica */
    private Double latitud;

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /** Longitud geográfica */
    private Double longitud;

    /** Estado actual del recurso */
    private String estado;

    /** Fecha de última actualización (generada automáticamente) */
    private LocalDateTime fechaActualizacion;
}
