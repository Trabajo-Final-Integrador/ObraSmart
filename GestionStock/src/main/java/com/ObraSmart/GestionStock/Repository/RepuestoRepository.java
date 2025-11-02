package com.ObraSmart.GestionStock.Repository;

import com.ObraSmart.GestionStock.Entity.Repuesto;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio Spring Data JPA para Repuesto.
 *
 * Métodos personalizados usados por el servicio:
 * - findByActivoTrue: devuelve solo repuestos activos (baja lógica).
 * - findByIdAndActivoTrue: buscar un repuesto activo por id.
 * - findByNombreAndActivoTrue: validación de nombres duplicados.
 * - findByCantidadLessThanAndActivoTrue: buscar stock por debajo de umbral.
 *
 * También incluye un método con 'PESSIMISTIC_WRITE' para bloquear fila cuando se modifica stock
 * y evitar condiciones de carrera en operaciones concurrentes.
 */
@Repository
public interface RepuestoRepository extends JpaRepository<Repuesto, Long> {

    List<Repuesto> findByActivoTrue();

    Optional<Repuesto> findByIdAndActivoTrue(Long id);

    Optional<Repuesto> findByNombreAndActivoTrue(String nombre);

    List<Repuesto> findByCantidadLessThanAndActivoTrue(int cantidad);

    long countByActivoTrue();

    long countByCantidadLessThanAndActivoTrue(int cantidad);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Repuesto r where r.id = :id and r.activo = true")
    Optional<Repuesto> findByIdForUpdate(@Param("id") Long id);
}
