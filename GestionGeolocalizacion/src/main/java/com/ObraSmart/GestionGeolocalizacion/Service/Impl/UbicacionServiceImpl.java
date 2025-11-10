package com.ObraSmart.GestionGeolocalizacion.Service.Impl;

import com.ObraSmart.GestionGeolocalizacion.Dto.UbicacionDto;
import com.ObraSmart.GestionGeolocalizacion.Entity.UbicacionEntity;
import com.ObraSmart.GestionGeolocalizacion.Repository.UbicacionRepository;
import com.ObraSmart.GestionGeolocalizacion.Service.IGeocodingService;
import com.ObraSmart.GestionGeolocalizacion.Service.IUbicacionService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio principal para la gestión de ubicaciones dentro del sistema ObraSmart.
 *
 * Se encarga de:
 *  - Guardar y actualizar ubicaciones en base de datos.
 *  - Consultar por estado o ID.
 *  - Obtener coordenadas reales mediante el servicio de geocodificación.
 */
@Service
public class UbicacionServiceImpl implements IUbicacionService {

    private final UbicacionRepository ubicacionRepository;
    private final IGeocodingService geocodingService;
    private final ModelMapper mapper;

    public UbicacionServiceImpl(UbicacionRepository ubicacionRepository,
                                IGeocodingService geocodingService) {
        this.ubicacionRepository = ubicacionRepository;
        this.geocodingService = geocodingService;
        this.mapper = new ModelMapper();
    }

    /**
     * Retorna todas las ubicaciones registradas.
     * Endpoint sugerido: GET /api/ubicaciones
     */
    @Override
    public List<UbicacionDto> obtenerTodas() {
        return ubicacionRepository.findAll()
                .stream()
                .map(entity -> mapper.map(entity, UbicacionDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Busca una ubicación por su ID.
     * Endpoint sugerido: GET /api/ubicaciones/{id}
     */
    @Override
    public Optional<UbicacionDto> obtenerPorId(Long id) {
        return ubicacionRepository.findById(id)
                .map(entity -> mapper.map(entity, UbicacionDto.class));
    }

    /**
     * Retorna una lista de ubicaciones filtradas por estado.
     * Ejemplo: EN_SERVICIO, FUERA_DE_SERVICIO, EN_MANTENIMIENTO.
     * Endpoint sugerido: GET /api/ubicaciones/estado/{estado}
     */
    @Override
    public List<UbicacionDto> obtenerPorEstado(String estado) {
        return ubicacionRepository.findByEstado(estado)
                .stream()
                .map(entity -> mapper.map(entity, UbicacionDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Crea una nueva ubicación.
     * Antes de guardar, obtiene las coordenadas geográficas usando geocoding.
     * Endpoint sugerido: POST /api/ubicaciones
     */
    @Override
    public UbicacionDto crear(UbicacionDto dto) {
        // Obtener coordenadas con el servicio de geocodificación
        double[] coords = geocodingService.obtenerCoordenadas(dto.getDireccion());
        if (coords != null) {
            dto.setLatitud(coords[0]);
            dto.setLongitud(coords[1]);
        }

        UbicacionEntity entity = mapper.map(dto, UbicacionEntity.class);
        UbicacionEntity guardado = ubicacionRepository.save(entity);
        return mapper.map(guardado, UbicacionDto.class);
    }

    /**
     * Actualiza una ubicación existente.
     * Si la dirección cambió, recalcula las coordenadas geográficas.
     * Endpoint sugerido: PUT /api/ubicaciones/{id}
     */
    @Override
    public UbicacionDto actualizar(Long id, UbicacionDto dto) {
        Optional<UbicacionEntity> existente = ubicacionRepository.findById(id);
        if (existente.isEmpty()) return null;

        UbicacionEntity entity = existente.get();

        // Guardar la dirección anterior antes de modificarla
        String direccionAnterior = entity.getDireccion();

        // Actualizar campos básicos
        entity.setReferencia(dto.getReferencia());
        entity.setTipo(dto.getTipo());
        entity.setDireccion(dto.getDireccion());
        entity.setEstado(dto.getEstado());

        // Recalcular coordenadas solo si la dirección cambió
        if (dto.getDireccion() != null && !dto.getDireccion().equals(direccionAnterior)) {
            double[] coords = geocodingService.obtenerCoordenadas(dto.getDireccion());
            if (coords != null) {
                entity.setLatitud(coords[0]);
                entity.setLongitud(coords[1]);
            }
        }

        UbicacionEntity actualizada = ubicacionRepository.save(entity);
        return mapper.map(actualizada, UbicacionDto.class);
    }

    /**
     * Elimina una ubicación por ID.
     * Endpoint sugerido: DELETE /api/ubicaciones/{id}
     */
    @Override
    public void eliminar(Long id) {
        ubicacionRepository.deleteById(id);
    }
}
