package com.ObraSmart.GestionEquipos.repository;

import com.ObraSmart.GestionEquipos.entity.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    boolean existsByNumeroSerie(String numeroSerie);

    boolean existsByNumeroPatente(String numeroPatente);
}
