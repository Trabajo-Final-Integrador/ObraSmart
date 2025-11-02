package com.ObraSmart.GestionStock.Repository;

import com.ObraSmart.GestionStock.Entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio Spring Data JPA para Proveedor (CRUD simple).
 */
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
}
