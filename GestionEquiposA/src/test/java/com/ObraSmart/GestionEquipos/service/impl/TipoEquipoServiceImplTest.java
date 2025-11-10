package com.ObraSmart.GestionEquipos.service.impl;

import com.ObraSmart.GestionEquipos.dtos.TipoEquipoDTO;
import com.ObraSmart.GestionEquipos.entity.TipoEquipo;
import com.ObraSmart.GestionEquipos.repository.TipoEquipoRepository;
import org.junit.jupiter.api.BeforeEach;
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

class TipoEquipoServiceImplTest {

    @Mock
    private TipoEquipoRepository tipoEquipoRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TipoEquipoServiceImpl tipoEquipoService;

    private TipoEquipo tipoEquipo;
    private TipoEquipoDTO tipoEquipoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        tipoEquipo = new TipoEquipo();
        tipoEquipo.setId(1L);
        tipoEquipo.setNombre("Excavadora");

        tipoEquipoDTO = new TipoEquipoDTO();
        tipoEquipoDTO.setId(1L);
        tipoEquipoDTO.setNombre("Excavadora");
    }

    @Test
    void getAll() {
        List<TipoEquipo> entities = Arrays.asList(tipoEquipo);
        when(tipoEquipoRepository.findAll()).thenReturn(entities);
        when(modelMapper.map(tipoEquipo, TipoEquipoDTO.class)).thenReturn(tipoEquipoDTO);

        // Act
        List<TipoEquipoDTO> result = tipoEquipoService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Excavadora", result.get(0).getNombre());
    }

    @Test
    void getById() {
        when(tipoEquipoRepository.findById(1L)).thenReturn(Optional.of(tipoEquipo));
        when(modelMapper.map(tipoEquipo, TipoEquipoDTO.class)).thenReturn(tipoEquipoDTO);

        TipoEquipoDTO result = tipoEquipoService.getById(1L);

        assertNotNull(result);
        assertEquals("Excavadora", result.getNombre());
        assertEquals(1L, result.getId());
    }

    @Test
    void create() {
        when(modelMapper.map(tipoEquipoDTO, TipoEquipo.class)).thenReturn(tipoEquipo);
        when(tipoEquipoRepository.save(tipoEquipo)).thenReturn(tipoEquipo);
        when(modelMapper.map(tipoEquipo, TipoEquipoDTO.class)).thenReturn(tipoEquipoDTO);

        TipoEquipoDTO result = tipoEquipoService.create(tipoEquipoDTO);

        assertNotNull(result);
        assertEquals("Excavadora", result.getNombre());
    }

    @Test
    void update() {
        TipoEquipoDTO dtoUpdate = new TipoEquipoDTO();
        dtoUpdate.setNombre("Retroexcavadora");

        when(tipoEquipoRepository.findById(1L)).thenReturn(Optional.of(tipoEquipo));
        when(tipoEquipoRepository.save(tipoEquipo)).thenReturn(tipoEquipo);
        when(modelMapper.map(tipoEquipo, TipoEquipoDTO.class)).thenReturn(dtoUpdate);

        TipoEquipoDTO result = tipoEquipoService.update(1L, dtoUpdate);

        assertNotNull(result);
        assertEquals("Retroexcavadora", result.getNombre());
    }

    @Test
    void delete() {
        when(tipoEquipoRepository.findById(1L)).thenReturn(Optional.of(tipoEquipo));

        assertDoesNotThrow(() -> tipoEquipoService.delete(1L));
    }
}