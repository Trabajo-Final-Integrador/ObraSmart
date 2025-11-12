package com.ObraSmart.GestionGeolocalizacion.Repository;

import com.ObraSmart.GestionGeolocalizacion.Entity.EquipoUbicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para acceder a ubicaciones de equipos.
 */
@Repository
public interface EquipoUbicacionRepository extends JpaRepository<EquipoUbicacion, Long> {
    List<EquipoUbicacion> findByEquipoId(Long equipoId);
}
