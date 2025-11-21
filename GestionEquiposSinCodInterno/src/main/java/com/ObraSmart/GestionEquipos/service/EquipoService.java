package com.ObraSmart.GestionEquipos.service;

import com.ObraSmart.GestionEquipos.dtos.EquipoDTO;

import java.util.List;

public interface EquipoService {

    List<EquipoDTO> getAll();

    EquipoDTO getById(Long id);

    EquipoDTO create(EquipoDTO dto);

    EquipoDTO update(Long id, EquipoDTO dto);

    void delete(Long id);
}
