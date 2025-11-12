package com.ObraSmart.GestionEquipos.service;

import com.ObraSmart.GestionEquipos.dtos.ModeloDTO;
import com.ObraSmart.GestionEquipos.dtos.TipoEquipoDTO;

import java.util.List;

public interface TipoEquipoService {
    List<TipoEquipoDTO> getAll();
    TipoEquipoDTO getById(Long id);
    TipoEquipoDTO create(TipoEquipoDTO dto);
    TipoEquipoDTO update(Long id, TipoEquipoDTO dto);
    void delete(Long id);
}
