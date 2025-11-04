package com.ObraSmart.GestionStock.Dto;

import lombok.*;

/**
 * DTO para transferir datos de Repuesto entre cliente y servidor.
 * Mantiene solo los campos que queremos exponer (no las relaciones completas).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepuestoDto {

    private Long id;
    private String nombre;
    private String categoria;
    private int cantidad;
    private String unidad;


    private Integer stockMinimo;
    private Long proveedorId;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }

    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    public Long getProveedorId() { return proveedorId; }
    public void setProveedorId(Long proveedorId) { this.proveedorId = proveedorId; }
}
