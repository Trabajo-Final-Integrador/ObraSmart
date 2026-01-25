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
import static org.mockito.Mockito.*;

class ModeloServiceImplTest {

    @Mock
    private ModeloRepository repo;

    @Mock
    private MarcaRepository marcaRepo;

    @InjectMocks
    private ModeloServiceImpl service;

    private ModelMapper mapper = new ModelMapper();

    private Marca marca;
    private Modelo modelo;
    private ModeloDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ModeloServiceImpl(repo, mapper, marcaRepo);

        marca = new Marca();
        marca.setId(1L);
        marca.setNombre("Caterpillar");

        modelo = new Modelo();
        modelo.setId(1L);
        modelo.setNombre("320D");
        modelo.setMarca(marca);

        dto = new ModeloDTO(1L, "320D", new MarcaDTO(1L, "Caterpillar"));
    }

    @Test
    @DisplayName("getAll devuelve lista")
    void testGetAll() {
        when(repo.findAll()).thenReturn(Arrays.asList(modelo));

        List<ModeloDTO> result = service.getAll();

        assertEquals(1, result.size());
        assertEquals("320D", result.get(0).getNombre());
    }

    @Test
    @DisplayName("getById devuelve DTO")
    void testGetById() {
        when(repo.findById(1L)).thenReturn(Optional.of(modelo));

        ModeloDTO result = service.getById(1L);

        assertEquals("320D", result.getNombre());
    }

    @Test
    @DisplayName("getById lanza error si no existe")
    void testGetById_NotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getById(1L));
    }

    @Test
    @DisplayName("create guarda modelo")
    void testCreate() {
        when(marcaRepo.findById(1L)).thenReturn(Optional.of(marca));
        when(repo.save(any())).thenReturn(modelo);

        ModeloDTO result = service.create(dto);

        assertEquals("320D", result.getNombre());
    }

    @Test
    @DisplayName("update modifica modelo")
    void testUpdate() {
        when(repo.findById(1L)).thenReturn(Optional.of(modelo));
        when(marcaRepo.findById(1L)).thenReturn(Optional.of(marca));
        when(repo.save(any())).thenReturn(modelo);

        ModeloDTO result = service.update(1L, dto);

        assertEquals("320D", result.getNombre());
    }

    @Test
    @DisplayName("update lanza error si no existe")
    void testUpdate_NotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(1L, dto));
    }

    @Test
    @DisplayName("delete funciona")
    void testDelete() {
        when(repo.findById(1L)).thenReturn(Optional.of(modelo));

        assertDoesNotThrow(() -> service.delete(1L));
    }
}
