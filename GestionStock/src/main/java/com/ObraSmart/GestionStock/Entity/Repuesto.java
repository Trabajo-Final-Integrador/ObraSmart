package com.ObraSmart.GestionStock.Entity;

import jakarta.persistence.*;
import lombok.*;

/*
 * Clase que representa la tabla "repuesto" en la base de datos.
 * Cada objeto de esta clase será un registro.
 * Se usa en el repositorio y se envía al controlador como datos.
 * @Builder.Default - Para que activo sea true por defecto
 * Campo activo - Para bajas lógicas (no eliminación física)
 * Métodos de conveniencia - desactivar(), reactivar(), estaActivo()
 * @Table(name = "repuestos") - Nombre explícito de tabla
 */

@Entity
@Table(name = "repuestos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Repuesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String categoria; // Ej: "Aceite", "Refrigerante", "Filtro"
    private int cantidad;
    private String unidad; // Ej: "litros", "unidades"

    // 🔹 NUEVO CAMPO PARA BAJAS LÓGICAS
    @Builder.Default
    private Boolean activo = true;

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

    // 🔹 NUEVOS GETTERS Y SETTERS PARA ACTIVO
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    // 🔹 MÉTODO CONVENIENCIA PARA BAJA LÓGICA
    public void desactivar() {
        this.activo = false;
    }

    // 🔹 MÉTODO CONVENIENCIA PARA REACTIVAR
    public void reactivar() {
        this.activo = true;
    }

    // 🔹 MÉTODO PARA VERIFICAR SI ESTÁ ACTIVO
    public boolean estaActivo() {
        return Boolean.TRUE.equals(activo);
    }
}