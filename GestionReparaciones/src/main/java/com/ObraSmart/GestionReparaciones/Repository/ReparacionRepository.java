package com.ObraSmart.GestionReparaciones.Repository;


import com.ObraSmart.GestionReparaciones.Entity.EstadoReparacion;
import com.ObraSmart.GestionReparaciones.Entity.Reparacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReparacionRepository extends JpaRepository<Reparacion, Long> {

    List<Reparacion> findByEquipoId(Long equipoId);

    List<Reparacion> findByEstadoReparacion(EstadoReparacion estadoReparacion);
}
