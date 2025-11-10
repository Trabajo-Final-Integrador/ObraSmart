package com.ObraSmart.GestionGeolocalizacion.Dto;

/**
 * DTO que devuelve este microservicio para que el frontend muestre los marcadores.
 * Clase compacta con solo lo necesario para el mapa.
 */
public class EquipoMapaDto {
    private Long id;
    private String nombre;
    private String estado; // DISPONIBLE, EN_MANTENIMIENTO, FUERA_DE_SERVICIO, ASIGNADO
    private Double lat;
    private Double lon;
    private String info; // texto adicional para popup

    public EquipoMapaDto() {}

    public EquipoMapaDto(Long id, String nombre, String estado, Double lat, Double lon, String info) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
        this.lat = lat;
        this.lon = lon;
        this.info = info;
    }

    // getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }

    public Double getLon() { return lon; }
    public void setLon(Double lon) { this.lon = lon; }

    public String getInfo() { return info; }
    public void setInfo(String info) { this.info = info; }
}
