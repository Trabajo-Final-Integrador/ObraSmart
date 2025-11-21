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
import static org.mockito.Mockito.*;

class MarcaServiceImplTest {

    @Mock
    private MarcaRepository repo;

    @InjectMocks
    private MarcaServiceImpl service;

    private ModelMapper modelMapper = new ModelMapper();

    private Marca marca;
    private MarcaDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new MarcaServiceImpl(repo, modelMapper);

        marca = new Marca();
        marca.setId(1L);
        marca.setNombre("Caterpillar");

        dto = new MarcaDTO(1L, "Caterpillar");
    }

    @Test
    @DisplayName("getAll devuelve lista de marcas")
    void testGetAll() {
        when(repo.findAll()).thenReturn(Arrays.asList(marca));

        List<MarcaDTO> result = service.getAll();

        assertEquals(1, result.size());
        assertEquals("Caterpillar", result.get(0).getNombre());
    }

    @Test
    @DisplayName("getById devuelve DTO")
    void testGetById() {
        when(repo.findById(1L)).thenReturn(Optional.of(marca));

        MarcaDTO result = service.getById(1L);

        assertEquals("Caterpillar", result.getNombre());
    }

    @Test
    @DisplayName("getById lanza error si no existe")
    void testGetById_NotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getById(1L));
    }

    @Test
    @DisplayName("create guarda correctamente")
    void testCreate() {
        when(repo.save(any())).thenReturn(marca);

        MarcaDTO result = service.create(dto);

        assertEquals("Caterpillar", result.getNombre());
    }

    @Test
    @DisplayName("update modifica marca")
    void testUpdate() {
        when(repo.findById(1L)).thenReturn(Optional.of(marca));
        when(repo.save(any())).thenReturn(marca);

        MarcaDTO result = service.update(1L, dto);

        assertEquals("Caterpillar", result.getNombre());
    }

    @Test
    @DisplayName("update lanza error si no existe")
    void testUpdate_NotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(1L, dto));
    }

    @Test
    @DisplayName("delete elimina sin errores")
    void testDelete() {
        when(repo.findById(1L)).thenReturn(Optional.of(marca));

        assertDoesNotThrow(() -> service.delete(1L));
    }
}
