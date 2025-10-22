package com.ObraSmart.GestionStock.Dto;


import lombok.*;

/**
 * DTO: Objeto que se usa para transferir datos entre cliente y servidor.
 * Contiene solo los campos que queremos exponer públicamente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepuestoDto {

    private Long id;

    private String nombre;
    private String categoria; // Ej: "Aceite", "Refrigerante", "Filtro"
    private int cantidad;
    private String unidad; // Ej: "litros", "unidades"

    // 🔹 GETTERS Y SETTERS MANUALES
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
}
