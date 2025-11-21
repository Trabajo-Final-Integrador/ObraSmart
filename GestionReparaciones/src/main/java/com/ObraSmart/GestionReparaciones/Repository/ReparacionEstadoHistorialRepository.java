package com.ObraSmart.GestionReparaciones.Repository;



import com.ObraSmart.GestionReparaciones.Entity.ReparacionEstadoHistorial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReparacionEstadoHistorialRepository
        extends JpaRepository<ReparacionEstadoHistorial, Long> {

    List<ReparacionEstadoHistorial> findByReparacion_IdOrderByFechaCambioAsc(Long reparacionId);
}
