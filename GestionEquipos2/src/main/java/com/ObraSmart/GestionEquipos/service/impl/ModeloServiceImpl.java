package com.ObraSmart.GestionEquipos.service.impl;

import com.ObraSmart.GestionEquipos.dtos.MarcaDTO;
import com.ObraSmart.GestionEquipos.dtos.ModeloDTO;
import com.ObraSmart.GestionEquipos.entity.Marca;
import com.ObraSmart.GestionEquipos.entity.Modelo;
import com.ObraSmart.GestionEquipos.repository.MarcaRepository;
import com.ObraSmart.GestionEquipos.repository.ModeloRepository;
import com.ObraSmart.GestionEquipos.service.ModeloService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ModeloServiceImpl implements ModeloService {

    private final ModeloRepository modeloRepository;
    private final ModelMapper modelMapper;
    private final MarcaRepository marcaRepository;

    public   ModeloServiceImpl(ModeloRepository modeloRepository, ModelMapper modelMapper, MarcaRepository marcaRepository) {
        this.modeloRepository = modeloRepository;
        this.modelMapper = modelMapper;
        this.marcaRepository = marcaRepository;
    }

    @Override
    public List<ModeloDTO> getAll() {
        return modeloRepository.findAll()
                .stream()
                .map(modelo -> modelMapper.map(modelo, ModeloDTO.class))
                .toList();
    }

    @Override
    public ModeloDTO getById(Long id) {
        Modelo modelo = modeloRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Modelo no encontrado con ID: " + id));
        return modelMapper.map(modelo, ModeloDTO.class);
    }

    @Transactional
    @Override
    public ModeloDTO create(ModeloDTO dto) {
        Modelo entity = modelMapper.map(dto, Modelo.class);

        MarcaDTO marcaDTO = dto.getMarca();
        if (marcaDTO != null) {
            Marca marca = marcaRepository.findById(marcaDTO.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Marca no encontrada con ID: " + marcaDTO.getId()));
            entity.setMarca(marca);
        }

        Modelo saved = modeloRepository.save(entity);
        return modelMapper.map(saved, ModeloDTO.class);
    }

    @Transactional
    @Override
    public ModeloDTO update(Long id, ModeloDTO dto) {
        Modelo modelo = modeloRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Modelo no encontrado con ID: " + id));

        modelo.setNombre(dto.getNombre());
        MarcaDTO marcaDTO = dto.getMarca();
        if (marcaDTO != null) {
            Marca marca = marcaRepository.findById(marcaDTO.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Marca no encontrada con ID: " + marcaDTO.getId()));
            modelo.setMarca(marca);
        }

        Modelo updated = modeloRepository.save(modelo);
        return modelMapper.map(updated, ModeloDTO.class);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Modelo modelo = modeloRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Modelo no encontrado con ID: " + id));
        modeloRepository.delete(modelo);
    }
}
