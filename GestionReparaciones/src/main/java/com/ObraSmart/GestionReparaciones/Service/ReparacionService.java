package com.ObraSmart.GestionReparaciones.Service;

import com.ObraSmart.GestionReparaciones.Dto.ReparacionRequestDto;
import com.ObraSmart.GestionReparaciones.Dto.ReparacionResponseDto;
import com.ObraSmart.GestionReparaciones.Entity.EstadoReparacion;

import java.util.List;

public interface ReparacionService {

    ReparacionResponseDto crearReparacion(ReparacionRequestDto request, Long usuarioAccionId);

    ReparacionResponseDto obtenerPorId(Long id);

    List<ReparacionResponseDto> listarTodas();

    List<ReparacionResponseDto> listarPorEquipo(Long equipoId);

    ReparacionResponseDto cambiarEstado(Long reparacionId,
                                        EstadoReparacion nuevoEstado,
                                        Long usuarioAccionId,
                                        String comentario);
}
