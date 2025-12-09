package com.ObraSmart.GestionReparaciones.service.impl;

import com.ObraSmart.GestionReparaciones.client.EquipoClient;
import com.ObraSmart.GestionReparaciones.client.UsuarioClient;
import com.ObraSmart.GestionReparaciones.dto.EquipoDTO;
import com.ObraSmart.GestionReparaciones.dto.UsuarioDTO;
import com.ObraSmart.GestionReparaciones.dto.ReparacionEstadoHistorialDto;
import com.ObraSmart.GestionReparaciones.dto.ReparacionRequestDto;
import com.ObraSmart.GestionReparaciones.dto.ReparacionResponseDto;
import com.ObraSmart.GestionReparaciones.entity.EstadoReparacion;
import com.ObraSmart.GestionReparaciones.entity.Reparacion;
import com.ObraSmart.GestionReparaciones.entity.ReparacionEstadoHistorial;
import com.ObraSmart.GestionReparaciones.exception.ResourceNotFoundException;
import com.ObraSmart.GestionReparaciones.repository.ReparacionEstadoHistorialRepository;
import com.ObraSmart.GestionReparaciones.repository.ReparacionRepository;
import com.ObraSmart.GestionReparaciones.service.GeocodingService;
import com.ObraSmart.GestionReparaciones.service.ReparacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReparacionServiceImpl implements ReparacionService {

    private final ReparacionRepository reparacionRepository;
    private final ReparacionEstadoHistorialRepository historialRepository;
    private final EquipoClient equipoClient;
    private final UsuarioClient usuarioClient;
    private final GeocodingService geocodingService;

    // ================== CREAR ==================
    @Override
    @Transactional
    public ReparacionResponseDto crearReparacion(ReparacionRequestDto request, Long usuarioAccionId) {

        // 1) Validar que el equipo existe
        equipoClient.validarEquipoExiste(request.getEquipoId());

        Reparacion reparacion = new Reparacion();
        reparacion.setEquipoId(request.getEquipoId());
        reparacion.setTipoMantenimiento(request.getTipoMantenimiento());
        reparacion.setDescripcion(request.getDescripcion());

        // estado inicial
        EstadoReparacion estadoInicial = request.getEstadoReparacion() != null
                ? request.getEstadoReparacion()
                : EstadoReparacion.CREADA;
        reparacion.setEstadoReparacion(estadoInicial);

        // responsable
        reparacion.setResponsableId(request.getResponsableId());

        // Obtener nombre del responsable desde el microservicio de usuarios
        String nombreCompleto = request.getResponsableNombreCompleto();
        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            try {
                UsuarioDTO usuario = usuarioClient.obtenerUsuario(request.getResponsableId());
                nombreCompleto = usuario.getNombreCompleto();
            } catch (Exception e) {
                nombreCompleto = "Usuario #" + request.getResponsableId();
            }
        }
        reparacion.setResponsableNombreCompleto(nombreCompleto);

        // geolocalizacion
        reparacion.setDireccion(request.getDireccion());
        if (request.getLat() != null && request.getLon() != null) {
            reparacion.setLat(request.getLat());
            reparacion.setLon(request.getLon());
        } else if (request.getDireccion() != null && !request.getDireccion().isBlank()) {
            double[] coord = geocodingService.geocodeAddress(request.getDireccion());
            if (coord != null) {
                reparacion.setLat(coord[0]);
                reparacion.setLon(coord[1]);
            }
        }

        reparacion.setFechaCreacion(LocalDateTime.now());
        reparacion.setFechaUltimaActualizacion(LocalDateTime.now());
        reparacion.setUsuarioUltimaActualizacionId(usuarioAccionId);

        Reparacion guardada = reparacionRepository.save(reparacion);

        // 2) Registrar historial de estado
        guardarHistorialEstado(null, estadoInicial, guardada, usuarioAccionId,
                "Reparacion creada");

        // 3) Actualizar estado del equipo (simple: si la reparacion se crea -> EN_MANTENIMIENTO)
        if (estadoInicial == EstadoReparacion.CREADA || estadoInicial == EstadoReparacion.EN_PROCESO) {
            equipoClient.actualizarEstadoEquipo(guardada.getEquipoId(), "EN_MANTENIMIENTO");
        }

        return mapToResponseDto(guardada);
    }

    // ================== ACTUALIZAR ==================
    @Override
    @Transactional
    public ReparacionResponseDto actualizarReparacion(Long id, ReparacionRequestDto request, Long usuarioAccionId) {

        Reparacion reparacion = reparacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reparacion no encontrada con id " + id));

        EstadoReparacion estadoAnterior = reparacion.getEstadoReparacion();
        EstadoReparacion nuevoEstado = request.getEstadoReparacion();
        boolean cambioEstado = nuevoEstado != null && !nuevoEstado.equals(estadoAnterior);

        // Validar que el equipo existe si se esta cambiando
        if (!reparacion.getEquipoId().equals(request.getEquipoId())) {
            equipoClient.validarEquipoExiste(request.getEquipoId());
            reparacion.setEquipoId(request.getEquipoId());
        }

        // Actualizar campos
        reparacion.setTipoMantenimiento(request.getTipoMantenimiento());
        reparacion.setDescripcion(request.getDescripcion());
        reparacion.setResponsableId(request.getResponsableId());

        // Obtener nombre del responsable si cambió
        String nombreCompleto = request.getResponsableNombreCompleto();
        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            try {
                UsuarioDTO usuario = usuarioClient.obtenerUsuario(request.getResponsableId());
                nombreCompleto = usuario.getNombreCompleto();
            } catch (Exception e) {
                nombreCompleto = "Usuario #" + request.getResponsableId();
            }
        }
        reparacion.setResponsableNombreCompleto(nombreCompleto);

        reparacion.setDireccion(request.getDireccion());

        // Actualizar geolocalizacion si es necesario
        if (request.getLat() != null && request.getLon() != null) {
            reparacion.setLat(request.getLat());
            reparacion.setLon(request.getLon());
        } else if (request.getDireccion() != null && !request.getDireccion().isBlank()) {
            double[] coord = geocodingService.geocodeAddress(request.getDireccion());
            if (coord != null) {
                reparacion.setLat(coord[0]);
                reparacion.setLon(coord[1]);
            }
        }

        if (cambioEstado) {
            reparacion.setEstadoReparacion(nuevoEstado);

            if (estadoAnterior == EstadoReparacion.CREADA
                    && nuevoEstado == EstadoReparacion.EN_PROCESO
                    && reparacion.getFechaInicio() == null) {
                reparacion.setFechaInicio(LocalDateTime.now());
            }

            if (nuevoEstado == EstadoReparacion.FINALIZADA || nuevoEstado == EstadoReparacion.CANCELADA) {
                reparacion.setFechaFin(LocalDateTime.now());
            }
        }

        reparacion.setFechaUltimaActualizacion(LocalDateTime.now());
        reparacion.setUsuarioUltimaActualizacionId(usuarioAccionId);

        Reparacion actualizada = reparacionRepository.save(reparacion);

        if (cambioEstado) {
            guardarHistorialEstado(estadoAnterior, nuevoEstado, actualizada, usuarioAccionId,
                    "Estado actualizado desde edicion");

            if (nuevoEstado == EstadoReparacion.FINALIZADA || nuevoEstado == EstadoReparacion.CANCELADA) {
                equipoClient.actualizarEstadoEquipo(actualizada.getEquipoId(), "DISPONIBLE");
            } else if (nuevoEstado == EstadoReparacion.CREADA || nuevoEstado == EstadoReparacion.EN_PROCESO) {
                equipoClient.actualizarEstadoEquipo(actualizada.getEquipoId(), "EN_MANTENIMIENTO");
            }
        }

        return mapToResponseDto(actualizada);
    }

    // ================== OBTENER ==================
    @Override
    @Transactional(readOnly = true)
    public ReparacionResponseDto obtenerPorId(Long id) {
        Reparacion rep = reparacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reparacion no encontrada con id " + id));

        return mapToResponseDto(rep);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReparacionResponseDto> listarTodas() {
        return reparacionRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReparacionResponseDto> listarPorEquipo(Long equipoId) {
        return reparacionRepository.findByEquipoId(equipoId)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    // ================== CAMBIAR ESTADO ==================
    @Override
    @Transactional
    public ReparacionResponseDto cambiarEstado(Long reparacionId,
                                               EstadoReparacion nuevoEstado,
                                               Long usuarioAccionId,
                                               String comentario) {

        Reparacion rep = reparacionRepository.findById(reparacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Reparacion no encontrada con id " + reparacionId));

        EstadoReparacion estadoAnterior = rep.getEstadoReparacion();
        rep.setEstadoReparacion(nuevoEstado);
        rep.setFechaUltimaActualizacion(LocalDateTime.now());
        rep.setUsuarioUltimaActualizacionId(usuarioAccionId);

        if (estadoAnterior == EstadoReparacion.CREADA && nuevoEstado == EstadoReparacion.EN_PROCESO) {
            rep.setFechaInicio(LocalDateTime.now());
        }

        if (nuevoEstado == EstadoReparacion.FINALIZADA || nuevoEstado == EstadoReparacion.CANCELADA) {
            rep.setFechaFin(LocalDateTime.now());
        }

        // Guardar cambios
        reparacionRepository.save(rep);

        // Historial
        guardarHistorialEstado(estadoAnterior, nuevoEstado, rep, usuarioAccionId, comentario);

        // Estado del equipo
        if (nuevoEstado == EstadoReparacion.FINALIZADA || nuevoEstado == EstadoReparacion.CANCELADA) {
            equipoClient.actualizarEstadoEquipo(rep.getEquipoId(), "DISPONIBLE");
        }

        return mapToResponseDto(rep);
    }

    // ================== Helpers ==================
    private void guardarHistorialEstado(EstadoReparacion anterior,
                                        EstadoReparacion nuevo,
                                        Reparacion rep,
                                        Long usuarioId,
                                        String comentario) {

        ReparacionEstadoHistorial h = new ReparacionEstadoHistorial();
        h.setReparacion(rep);
        h.setEstadoAnterior(anterior);
        h.setEstadoNuevo(nuevo);
        h.setUsuarioResponsableId(usuarioId);
        h.setComentario(comentario);

        historialRepository.save(h);
    }

    private ReparacionResponseDto mapToResponseDto(Reparacion rep) {

        ReparacionResponseDto dto = new ReparacionResponseDto();
        dto.setId(rep.getId());
        dto.setEquipoId(rep.getEquipoId());
        dto.setTipoMantenimiento(rep.getTipoMantenimiento());
        dto.setEstadoReparacion(rep.getEstadoReparacion());
        dto.setDescripcion(rep.getDescripcion());

        dto.setResponsableId(rep.getResponsableId());
        dto.setResponsableNombreCompleto(rep.getResponsableNombreCompleto());

        dto.setDireccion(rep.getDireccion());
        dto.setLat(rep.getLat());
        dto.setLon(rep.getLon());

        dto.setFechaCreacion(rep.getFechaCreacion());
        dto.setFechaInicio(rep.getFechaInicio());
        dto.setFechaFin(rep.getFechaFin());
        dto.setFechaUltimaActualizacion(rep.getFechaUltimaActualizacion());
        dto.setUsuarioUltimaActualizacionId(rep.getUsuarioUltimaActualizacionId());

        // Datos del equipo
        try {
            EquipoDTO equipo = equipoClient.obtenerEquipo(rep.getEquipoId());

            dto.setEquipoNombre(equipo.getNombre());
            dto.setEquipoCodigoInterno(equipo.getCodigoInterno());

        } catch (Exception e) {
            dto.setEquipoNombre("Desconocido");
            dto.setEquipoCodigoInterno("-");
        }

        // Historial de estados
        var historial = historialRepository
                .findByReparacion_IdOrderByFechaCambioAsc(rep.getId())
                .stream()
                .map(h -> {
                    ReparacionEstadoHistorialDto hd = new ReparacionEstadoHistorialDto();
                    hd.setId(h.getId());
                    hd.setEstadoAnterior(h.getEstadoAnterior());
                    hd.setEstadoNuevo(h.getEstadoNuevo());
                    hd.setFechaCambio(h.getFechaCambio());
                    hd.setUsuarioResponsableId(h.getUsuarioResponsableId());
                    hd.setComentario(h.getComentario());
                    return hd;
                })
                .toList();

        dto.setHistorialEstados(historial);

        return dto;
    }

}
