package com.ObraSmart.GestionGeolocalizacion.Repository;


import com.ObraSmart.GestionGeolocalizacion.Entity.UbicacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UbicacionRepository extends JpaRepository<UbicacionEntity, Long> {
    List<UbicacionEntity> findByTipo(String tipo);
    List<UbicacionEntity> findByEstado(String estado);
}
