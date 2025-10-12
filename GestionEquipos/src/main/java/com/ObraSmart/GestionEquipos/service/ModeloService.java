package com.ObraSmart.GestionEquipos.service;

import com.ObraSmart.GestionEquipos.dtos.ModeloDTO;

import java.util.List;

public interface ModeloService {
    List<ModeloDTO> getAll();
    ModeloDTO getById(Long id);
    ModeloDTO create(ModeloDTO dto);
    ModeloDTO update(Long id, ModeloDTO dto);
    void delete(Long id);
}
