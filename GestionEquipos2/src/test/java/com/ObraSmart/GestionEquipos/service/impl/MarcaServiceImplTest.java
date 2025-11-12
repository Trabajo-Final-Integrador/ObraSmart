package com.ObraSmart.GestionEquipos.service.impl;

import com.ObraSmart.GestionEquipos.dtos.MarcaDTO;
import com.ObraSmart.GestionEquipos.entity.Marca;
import com.ObraSmart.GestionEquipos.repository.MarcaRepository;
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

class MarcaServiceImplTest {

    @Mock
    private MarcaRepository marcaRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MarcaServiceImpl marcaService;

    private Marca marca;
    private MarcaDTO marcaDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        marca = new Marca();
        marca.setId(1L);
        marca.setNombre("Caterpillar");

        marcaDTO = new MarcaDTO();
        marcaDTO.setId(1L);
        marcaDTO.setNombre("Caterpillar");
    }

    @Test
    void getAll() {
        when(marcaRepository.findAll()).thenReturn(Arrays.asList(marca));
        when(modelMapper.map(marca, MarcaDTO.class)).thenReturn(marcaDTO);

        List<MarcaDTO> result = marcaService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Caterpillar", result.get(0).getNombre());
    }

    @Test
    void getById() {
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
        when(modelMapper.map(marca, MarcaDTO.class)).thenReturn(marcaDTO);

        MarcaDTO result = marcaService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Caterpillar", result.getNombre());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando no existe la marca al buscar por ID")
    void getById_ThrowsException_WhenNotFound() {
        when(marcaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> marcaService.getById(1L));
    }

    @Test
    void create() {
        when(modelMapper.map(marcaDTO, Marca.class)).thenReturn(marca);
        when(marcaRepository.save(marca)).thenReturn(marca);
        when(modelMapper.map(marca, MarcaDTO.class)).thenReturn(marcaDTO);

        MarcaDTO result = marcaService.create(marcaDTO);

        assertNotNull(result);
        assertEquals("Caterpillar", result.getNombre());
        assertEquals(1L, result.getId());
    }

    @Test
    void update() {
        MarcaDTO dtoUpdate = new MarcaDTO();
        dtoUpdate.setNombre("John Deere");

        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
        when(marcaRepository.save(marca)).thenReturn(marca);
        when(modelMapper.map(marca, MarcaDTO.class)).thenReturn(dtoUpdate);

        MarcaDTO result = marcaService.update(1L, dtoUpdate);

        assertNotNull(result);
        assertEquals("John Deere", result.getNombre());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando no existe la marca al actualizar")
    void update_ThrowsException_WhenNotFound() {
        when(marcaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> marcaService.update(1L, marcaDTO));
    }

    @Test
    void delete() {
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));

        assertDoesNotThrow(() -> marcaService.delete(1L));
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar eliminar marca inexistente")
    void delete_ThrowsException_WhenNotFound() {
        when(marcaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> marcaService.delete(1L));
    }
}