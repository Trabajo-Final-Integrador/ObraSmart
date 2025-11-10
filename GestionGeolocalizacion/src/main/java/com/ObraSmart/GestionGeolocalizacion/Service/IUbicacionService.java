package com.ObraSmart.GestionGeolocalizacion.Service;


import com.ObraSmart.GestionGeolocalizacion.Dto.UbicacionDto;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz del servicio de gestión de ubicaciones.
 * Define las operaciones CRUD y la integración con el servicio de geocodificación.
 */
public interface IUbicacionService {

    List<UbicacionDto> obtenerTodas();

    Optional<UbicacionDto> obtenerPorId(Long id);

    List<UbicacionDto> obtenerPorEstado(String estado);

    UbicacionDto crear(UbicacionDto ubicacionDto);

    UbicacionDto actualizar(Long id, UbicacionDto ubicacionDto);

    void eliminar(Long id);
}
