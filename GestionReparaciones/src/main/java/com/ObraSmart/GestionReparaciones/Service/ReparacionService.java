package com.ObraSmart.GestionReparaciones.Service;


import com.ObraSmart.GestionReparaciones.Client.EquipoClient;
import com.ObraSmart.GestionReparaciones.Dto.ReparacionDto;
import com.ObraSmart.GestionReparaciones.Dto.ReparacionResponseDto;
import com.ObraSmart.GestionReparaciones.Exception.ResourceNotFoundException;
import com.ObraSmart.GestionReparaciones.Entity.Reparacion;
import com.ObraSmart.GestionReparaciones.Entity.EstadoReparacion;
import com.ObraSmart.GestionReparaciones.Repository.ReparacionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReparacionService {

    private final ReparacionRepository repo;
    private final GeocodingService geocodingService;
    private final EquipoClient equipoClient;

    public ReparacionService(ReparacionRepository repo, GeocodingService geocodingService, EquipoClient equipoClient) {
        this.repo = repo;
        this.geocodingService = geocodingService;
        this.equipoClient = equipoClient;
    }

    /**
     * Crea una reparación. Valida que el equipo exista consultando gestion-equipos.
     * Si viene direccion y no lat/lon -> intenta geocoding.
     */
    public ReparacionResponseDto crearReparacion(ReparacionDto dto) {
        // validar existencia de equipo
        EquipoClient.EquipoDto equipo = equipoClient.getEquipoById(dto.getEquipoId());
        if (equipo == null) {
            throw new ResourceNotFoundException("Equipo no encontrado: " + dto.getEquipoId());
        }

        Reparacion r = new Reparacion();
        r.setEquipoId(dto.getEquipoId());
        r.setDescripcion(dto.getDescripcion());
        r.setDireccion(dto.getDireccion());
        r.setFechaCreacion(LocalDateTime.now());
        r.setEstado(EstadoReparacion.EN_MANTENIMIENTO); // por defecto, ajustá si querés

        // Geocoding si hace falta
        if (dto.getDireccion() != null && (dto.getLat() == null || dto.getLon() == null)) {
            double[] coords = geocodingService.geocodeAddress(dto.getDireccion());
            if (coords != null) {
                r.setLat(coords[0]);
                r.setLon(coords[1]);
            }
        } else if (dto.getLat() != null && dto.getLon() != null) {
            r.setLat(dto.getLat());
            r.setLon(dto.getLon());
        }

        Reparacion saved = repo.save(r);
        return mapToResponse(saved);
    }

    public List<ReparacionResponseDto> listarTodas() {
        return repo.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public ReparacionResponseDto getById(Long id) {
        return repo.findById(id).map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Reparacion no encontrada: " + id));
    }

    // Mapear entidad -> DTO de respuesta
    private ReparacionResponseDto mapToResponse(Reparacion r) {
        ReparacionResponseDto resp = new ReparacionResponseDto();
        resp.setId(r.getId());
        resp.setEquipoId(r.getEquipoId());
        resp.setDescripcion(r.getDescripcion());
        resp.setEstado(r.getEstado() != null ? r.getEstado().name() : null);
        resp.setLat(r.getLat());
        resp.setLon(r.getLon());
        resp.setDireccion(r.getDireccion());
        resp.setFechaCreacion(r.getFechaCreacion());
        resp.setFechaInicio(r.getFechaInicio());
        resp.setFechaFin(r.getFechaFin());
        return resp;
    }
}

