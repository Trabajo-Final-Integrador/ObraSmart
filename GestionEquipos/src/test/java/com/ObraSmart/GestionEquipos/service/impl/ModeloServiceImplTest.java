package com.ObraSmart.GestionEquipos.service.impl;

import com.ObraSmart.GestionEquipos.dtos.MarcaDTO;
import com.ObraSmart.GestionEquipos.dtos.ModeloDTO;
import com.ObraSmart.GestionEquipos.entity.Marca;
import com.ObraSmart.GestionEquipos.entity.Modelo;
import com.ObraSmart.GestionEquipos.repository.MarcaRepository;
import com.ObraSmart.GestionEquipos.repository.ModeloRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ModeloServiceImplTest {
    @Mock
    private ModeloRepository modeloRepository;

    @Mock
    private MarcaRepository marcaRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ModeloServiceImpl modeloService;

    private Modelo modelo;
    private ModeloDTO modeloDTO;
    private Marca marca;
    private MarcaDTO marcaDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock Marca
        marca = new Marca();
        marca.setId(1L);
        marca.setNombre("Caterpillar");

        marcaDTO = new MarcaDTO();
        marcaDTO.setId(1L);
        marcaDTO.setNombre("Caterpillar");

        // Mock Modelo
        modelo = new Modelo();
        modelo.setId(1L);
        modelo.setNombre("Retroexcavadora ZX300");
        modelo.setMarca(marca);

        modeloDTO = new ModeloDTO();
        modeloDTO.setId(1L);
        modeloDTO.setNombre("Retroexcavadora ZX300");
        modeloDTO.setMarca(marcaDTO);
    }

    @Test
    void getAll() {
        when(modeloRepository.findAll()).thenReturn(Arrays.asList(modelo));
        when(modelMapper.map(modelo, ModeloDTO.class)).thenReturn(modeloDTO);

        List<ModeloDTO> result = modeloService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Retroexcavadora ZX300", result.get(0).getNombre());
        assertEquals("Caterpillar", result.get(0).getMarca().getNombre());
    }

    @Test
    void getById() {
        when(modeloRepository.findById(1L)).thenReturn(Optional.of(modelo));
        when(modelMapper.map(modelo, ModeloDTO.class)).thenReturn(modeloDTO);

        ModeloDTO result = modeloService.getById(1L);

        assertNotNull(result);
        assertEquals("Retroexcavadora ZX300", result.getNombre());
        assertEquals(1L, result.getId());
        assertEquals("Caterpillar", result.getMarca().getNombre());
    }

    @Test
    void create() {
        when(modelMapper.map(modeloDTO, Modelo.class)).thenReturn(modelo);
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
        when(modeloRepository.save(modelo)).thenReturn(modelo);
        when(modelMapper.map(modelo, ModeloDTO.class)).thenReturn(modeloDTO);

        ModeloDTO result = modeloService.create(modeloDTO);

        assertNotNull(result);
        assertEquals("Retroexcavadora ZX300", result.getNombre());
        assertEquals("Caterpillar", result.getMarca().getNombre());
    }

    @Test
    void create_ThrowsException_WhenMarcaNotFound() {
        when(modelMapper.map(modeloDTO, Modelo.class)).thenReturn(modelo);
        when(marcaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> modeloService.create(modeloDTO));
    }

    @Test
    void update() {
        ModeloDTO dtoUpdate = new ModeloDTO();
        dtoUpdate.setNombre("Retroexcavadora ZX400");
        dtoUpdate.setMarca(marcaDTO);

        when(modeloRepository.findById(1L)).thenReturn(Optional.of(modelo));
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
        when(modeloRepository.save(modelo)).thenReturn(modelo);
        when(modelMapper.map(modelo, ModeloDTO.class)).thenReturn(dtoUpdate);

        ModeloDTO result = modeloService.update(1L, dtoUpdate);

        assertNotNull(result);
        assertEquals("Retroexcavadora ZX400", result.getNombre());
        assertEquals("Caterpillar", result.getMarca().getNombre());
    }

    @Test
    void update_ThrowsException_WhenModeloNotFound() {
        when(modeloRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> modeloService.update(1L, modeloDTO));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la marca del update no existe")
    void update_ThrowsException_WhenMarcaNotFound() {
        when(modeloRepository.findById(1L)).thenReturn(Optional.of(modelo));
        when(marcaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> modeloService.update(1L, modeloDTO));
    }

    // ------------------------------------------------------------
    @Test
    @DisplayName("Debe eliminar un modelo existente sin errores")
    void delete_RemovesModeloSuccessfully() {
        when(modeloRepository.findById(1L)).thenReturn(Optional.of(modelo));

        assertDoesNotThrow(() -> modeloService.delete(1L));
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar eliminar modelo inexistente")
    void delete_ThrowsException_WhenNotFound() {
        when(modeloRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> modeloService.delete(1L));
    }
}