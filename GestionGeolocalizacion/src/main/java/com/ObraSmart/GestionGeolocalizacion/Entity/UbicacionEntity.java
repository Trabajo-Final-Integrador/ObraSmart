package com.ObraSmart.GestionGeolocalizacion.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Representa la ubicación geográfica de un equipo o una reparación dentro del sistema ObraSmart.
 * Este registro permite mostrar en mapa los estados de los recursos (en servicio, mantenimiento, etc.).
 */
@Entity
@Table(name = "ubicaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UbicacionEntity {

    /** Identificador autogenerado de la ubicación */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Referencia externa: puede ser el ID de un equipo o una reparación */
    @Column(nullable = false)
    private String referencia;

    /** Tipo de entidad asociada: "EQUIPO" o "REPARACION" */
    @Column(nullable = false)
    private String tipo;

    /** Dirección legible o texto descriptivo */
    private String direccion;

    /** Latitud geográfica */
    @Column(nullable = false)
    private double latitud;

    /** Longitud geográfica */
    @Column(nullable = false)
    private double longitud;

    /** Estado del recurso: "EN_SERVICIO", "EN_MANTENIMIENTO", "FUERA_DE_SERVICIO", etc. */
    @Column(nullable = false)
    private String estado;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
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

    /** Fecha de última actualización (para control o sincronización) */
    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    /** Actualiza automáticamente la fecha al modificar la entidad */
    @PreUpdate
    public void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
