package com.ObraSmart.GestionStock.Entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad JPA que representa un proveedor.
 * Responsabilidad:
 * - Almacena datos de proveedores usados para las órdenes de compra.
 */
@Entity
@Table(name = "proveedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    // datos de contacto
    private String contacto;
    private String telefono;
    private String email;
    private String direccion;

    // identificador fiscal (opcional)
    private String identificador;
}
