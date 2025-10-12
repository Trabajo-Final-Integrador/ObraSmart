package com.ObraSmart.GestionEquipos.service;

import com.ObraSmart.GestionEquipos.dtos.MarcaDTO;

import java.util.List;

public interface MarcaService {
    List<MarcaDTO> getAll();
    MarcaDTO getById(Long id);
    MarcaDTO create(MarcaDTO dto);
    MarcaDTO update(Long id, MarcaDTO dto);
    void delete(Long id);
}
