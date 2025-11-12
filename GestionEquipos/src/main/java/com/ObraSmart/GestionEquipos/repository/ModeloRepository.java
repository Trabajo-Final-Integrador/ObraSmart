package com.ObraSmart.GestionEquipos.repository;

import com.ObraSmart.GestionEquipos.entity.Marca;
import com.ObraSmart.GestionEquipos.entity.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Long> {
    boolean existsByNombreAndMarca(String nombre, Marca marca);
}
