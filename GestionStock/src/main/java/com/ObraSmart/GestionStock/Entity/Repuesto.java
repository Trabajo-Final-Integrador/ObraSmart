package com.ObraSmart.GestionStock.Entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad JPA que representa un repuesto (pieza / insumo).
 *
 * Responsabilidad:
 * - Mapea la tabla de repuestos en la BDD.
 * - Contiene el estado de stock (cantidad) y el control de baja lógica (activo).
 * - Relaciona el repuesto con su proveedor (ManyToOne).
 *
 * Notas:
 * - Se mantiene campo 'activo' para implementer baja lógica.
 * - stockMinimo guarda el umbral mínimo para alertas/compras.
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

    private String categoria;

    // cantidad actual en stock
    private int cantidad;

    private String unidad;

    //mínimo para reposición
    @Column(name = "stock_minimo")
    private Integer stockMinimo;

    // baja lógica
    private boolean activo;

    // relación con proveedor: un repuesto puede tener un proveedor (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;
}
