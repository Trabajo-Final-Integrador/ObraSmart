package com.ObraSmart.GestionObrador.service;

import com.ObraSmart.GestionObrador.dto.AsignarEquipoDto;
import com.ObraSmart.GestionObrador.dto.ObradorRequestDto;
import com.ObraSmart.GestionObrador.dto.ObradorResponseDto;

import java.util.List;

public interface ObradorService {

    List<ObradorResponseDto> listar();

    ObradorResponseDto obtenerPorId(Long id);

    ObradorResponseDto crear(ObradorRequestDto dto);

    ObradorResponseDto actualizar(Long id, ObradorRequestDto dto);

    ObradorResponseDto asignarEquipo(Long id, AsignarEquipoDto dto);
}
