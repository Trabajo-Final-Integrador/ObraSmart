package com.ObraSmart.GestionReparaciones.service;

import com.ObraSmart.GestionReparaciones.dto.ReparacionRequestDto;
import com.ObraSmart.GestionReparaciones.dto.ReparacionResponseDto;
import com.ObraSmart.GestionReparaciones.entity.EstadoReparacion;

import java.util.List;

public interface ReparacionService {

    ReparacionResponseDto crearReparacion(ReparacionRequestDto request, Long usuarioAccionId);

    ReparacionResponseDto actualizarReparacion(Long id, ReparacionRequestDto request, Long usuarioAccionId);

    ReparacionResponseDto obtenerPorId(Long id);

    List<ReparacionResponseDto> listarTodas();

    List<ReparacionResponseDto> listarPorEquipo(Long equipoId);

    ReparacionResponseDto cambiarEstado(Long reparacionId,
                                        EstadoReparacion nuevoEstado,
                                        Long usuarioAccionId,
                                        String comentario);
}
