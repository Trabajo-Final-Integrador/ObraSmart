package com.ObraSmart.GestionEquipos.service;

import com.ObraSmart.GestionEquipos.dtos.TipoEquipoDTO;
import java.util.List;

public interface TipoEquipoService {

    TipoEquipoDTO create(TipoEquipoDTO dto);

    TipoEquipoDTO update(Long id, TipoEquipoDTO dto);

    void delete(Long id);

    TipoEquipoDTO getById(Long id);

    List<TipoEquipoDTO> getAll();
}
