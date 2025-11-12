package com.ObraSmart.GestionGeolocalizacion.Dto;

/**
 * DTO para representar un equipo recibido desde el microservicio GestionEquipos.
 */
public class EquipoUbicacionDTO {
    private Long id;
    private String nombre;
    private String ubicacionActual;
    private String estado;

    public EquipoUbicacionDTO() {}

    public EquipoUbicacionDTO(Long id, String nombre, String ubicacionActual, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacionActual = ubicacionActual;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUbicacionActual() { return ubicacionActual; }
    public void setUbicacionActual(String ubicacionActual) { this.ubicacionActual = ubicacionActual; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
