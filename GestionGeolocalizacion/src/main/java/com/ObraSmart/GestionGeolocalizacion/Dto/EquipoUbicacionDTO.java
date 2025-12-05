package com.ObraSmart.GestionGeolocalizacion.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para representar un equipo recibido desde el microservicio GestionEquipos.
 */
public class EquipoUbicacionDTO {
    private Long id;
    private String nombre;
    private String ubicacionActual;

    @JsonProperty("estadoOperativo")
    private String estado;

    private Double latitud;
    private Double longitud;

    public EquipoUbicacionDTO() {}

    public EquipoUbicacionDTO(Long id, String nombre, String ubicacionActual, String estado, Double latitud, Double longitud) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacionActual = ubicacionActual;
        this.estado = estado;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUbicacionActual() { return ubicacionActual; }
    public void setUbicacionActual(String ubicacionActual) { this.ubicacionActual = ubicacionActual; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }
    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }
}
