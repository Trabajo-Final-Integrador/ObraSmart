package com.ObraSmart.GestionGeolocalizacion.Dto;

/**
 * DTO para enviar al frontend la ubicación geolocalizada de un equipo.
 */
public class EquipoUbicacionResponse {
    private Long id;
    private String nombre;
    private double lat;
    private double lon;
    private String estado;

    public EquipoUbicacionResponse() {}

    public EquipoUbicacionResponse(Long id, String nombre, double lat, double lon, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.lat = lat;
        this.lon = lon;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLon() { return lon; }
    public void setLon(double lon) { this.lon = lon; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
