package com.ObraSmart.GestionGeolocalizacion.Dto;

public class ReparacionLiteDTO {
    private Long id;
    private Long equipoId;
    private String estado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEquipoId() { return equipoId; }
    public void setEquipoId(Long equipoId) { this.equipoId = equipoId; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
