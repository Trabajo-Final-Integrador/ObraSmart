package com.ObraSmart.GestionEquipos.repository;

import com.ObraSmart.GestionEquipos.entity.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    boolean existsByNumeroSerie(String numeroSerie);

    boolean existsByCodigoInterno(String codigoInterno);

    // Para buscar el último código interno por prefijo de tipoEquipo
    Optional<Equipo> findTopByTipoEquipo_PrefijoOrderByCodigoInternoDesc(String prefijo);
}
