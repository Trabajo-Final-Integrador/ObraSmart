package com.ObraSmart.GestionEquipos.service.impl;

import com.ObraSmart.GestionEquipos.dtos.MarcaDTO;
import com.ObraSmart.GestionEquipos.entity.Marca;
import com.ObraSmart.GestionEquipos.repository.MarcaRepository;
import com.ObraSmart.GestionEquipos.service.MarcaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarcaServiceImpl implements MarcaService {

    private final MarcaRepository marcaRepository;
    private final ModelMapper modelMapper;

    public MarcaServiceImpl(MarcaRepository marcaRepository, ModelMapper modelMapper) {
        this.marcaRepository = marcaRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    public List<MarcaDTO> getAll() {
        return marcaRepository.findAll()
                .stream()
                .map(marca -> modelMapper.map(marca, MarcaDTO.class))
                .toList();
    }

    @Override
    public MarcaDTO getById(Long id) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Marca no encontrada con ID: " + id));
        return modelMapper.map(marca, MarcaDTO.class);
    }

    @Transactional
    @Override
    public MarcaDTO create(MarcaDTO dto) {
        Marca entity = modelMapper.map(dto, Marca.class);
        Marca saved = marcaRepository.save(entity);
        return modelMapper.map(saved, MarcaDTO.class);
    }

    @Transactional
    @Override
    public MarcaDTO update(Long id, MarcaDTO dto) {
        Marca existing = marcaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Marca no encontrada con ID: " + id));

        existing.setNombre(dto.getNombre());
        Marca updated = marcaRepository.save(existing);

        return modelMapper.map(updated, MarcaDTO.class);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Marca no encontrada con ID: " + id));
        marcaRepository.delete(marca);
    }
}
