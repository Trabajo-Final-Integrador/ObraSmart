package com.ObraSmart.GestionLogistica.service;

import com.ObraSmart.GestionLogistica.dto.TrasladoRequestDto;
import com.ObraSmart.GestionLogistica.dto.TrasladoResponseDto;

import java.util.List;

public interface TrasladoService {

    List<TrasladoResponseDto> listar();

    TrasladoResponseDto obtenerPorId(Long id);

    TrasladoResponseDto crear(TrasladoRequestDto dto);

    TrasladoResponseDto cambiarEstado(Long id, String nuevoEstado);
}
