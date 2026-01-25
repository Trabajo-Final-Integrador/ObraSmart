package com.ObraSmart.GestionReparaciones.repository;


import com.ObraSmart.GestionReparaciones.entity.EstadoReparacion;
import com.ObraSmart.GestionReparaciones.entity.Reparacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReparacionRepository extends JpaRepository<Reparacion, Long> {

    List<Reparacion> findByEquipoId(Long equipoId);

    List<Reparacion> findByEstadoReparacion(EstadoReparacion estadoReparacion);
}
