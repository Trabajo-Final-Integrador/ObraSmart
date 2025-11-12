package com.ObraSmart.GestionEquipos.service.impl;

import com.ObraSmart.GestionEquipos.dtos.MarcaDTO;
import com.ObraSmart.GestionEquipos.dtos.ModeloDTO;
import com.ObraSmart.GestionEquipos.dtos.TipoEquipoDTO;
import com.ObraSmart.GestionEquipos.entity.Marca;
import com.ObraSmart.GestionEquipos.entity.Modelo;
import com.ObraSmart.GestionEquipos.entity.TipoEquipo;
import com.ObraSmart.GestionEquipos.repository.TipoEquipoRepository;
import com.ObraSmart.GestionEquipos.service.TipoEquipoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TipoEquipoServiceImpl implements TipoEquipoService {

    private final TipoEquipoRepository tipoEquipoRepository;
    private final ModelMapper modelMapper;

    public TipoEquipoServiceImpl(TipoEquipoRepository tipoEquipoRepository, ModelMapper modelMapper) {
        this.tipoEquipoRepository = tipoEquipoRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    public List<TipoEquipoDTO> getAll() {
        return tipoEquipoRepository.findAll()
                .stream()
                .map(tipoEquipo -> modelMapper.map(tipoEquipo, TipoEquipoDTO.class))
                .toList();
    }

    @Override
    public TipoEquipoDTO getById(Long id) {
        TipoEquipo tipoEquipo = tipoEquipoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de equipo no encontrado con ID: " + id));
        return modelMapper.map(tipoEquipo, TipoEquipoDTO.class);
    }

    @Transactional
    @Override
    public TipoEquipoDTO create(TipoEquipoDTO dto) {
        TipoEquipo entity = modelMapper.map(dto, TipoEquipo.class);
        TipoEquipo saved = tipoEquipoRepository.save(entity);
        return modelMapper.map(saved, TipoEquipoDTO.class);
    }

    @Transactional
    @Override
    public TipoEquipoDTO update(Long id, TipoEquipoDTO dto) {
        TipoEquipo existing = tipoEquipoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de equipo no encontrado con ID: " + id));

        existing.setNombre(dto.getNombre());
        TipoEquipo updated = tipoEquipoRepository.save(existing);

        return modelMapper.map(updated, TipoEquipoDTO.class);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        TipoEquipo tipoEquipo = tipoEquipoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de equipo no encontrado con ID: " + id));
        tipoEquipoRepository.delete(tipoEquipo);
    }
}
