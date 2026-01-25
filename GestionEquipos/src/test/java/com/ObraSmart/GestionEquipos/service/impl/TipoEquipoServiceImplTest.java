package com.ObraSmart.GestionEquipos.service.impl;

import com.ObraSmart.GestionEquipos.dtos.TipoEquipoDTO;
import com.ObraSmart.GestionEquipos.entity.TipoEquipo;
import com.ObraSmart.GestionEquipos.exception.BusinessException;
import com.ObraSmart.GestionEquipos.exception.NotFoundException;
import com.ObraSmart.GestionEquipos.repository.TipoEquipoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TipoEquipoServiceImplTest {

    @Mock
    private TipoEquipoRepository repo;

    @InjectMocks
    private TipoEquipoServiceImpl service;

    private TipoEquipo tipo;
    private TipoEquipoDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        tipo = new TipoEquipo();
        tipo.setId(1L);
        tipo.setNombre("Retroexcavadora");
        tipo.setPrefijo("RET");

        dto = TipoEquipoDTO.builder()
                .id(1L)
                .nombre("Retroexcavadora")
                .prefijo("RET")
                .descripcion(null)
                .build();
    }


    @Test
    @DisplayName("create verifica validación de prefijo correcto")
    void testCreate_OK() {
        when(repo.save(any())).thenReturn(tipo);

        TipoEquipoDTO result = service.create(dto);

        assertEquals("RET", result.getPrefijo());
    }

    @Test
    @DisplayName("create lanza error si prefijo invalido")
    void testCreate_PrefijoInvalido() {
        dto.setPrefijo("RET123");

        BusinessException ex = assertThrows(BusinessException.class, () -> service.create(dto));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("getById devuelve DTO")
    void testGetById() {
        when(repo.findById(1L)).thenReturn(Optional.of(tipo));

        TipoEquipoDTO result = service.getById(1L);

        assertEquals("RET", result.getPrefijo());
    }

    @Test
    @DisplayName("getById lanza NotFound")
    void testGetById_NotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getById(1L));
    }

    @Test
    @DisplayName("getAll devuelve lista")
    void testGetAll() {
        when(repo.findAll()).thenReturn(Arrays.asList(tipo));

        List<TipoEquipoDTO> result = service.getAll();

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("delete funciona")
    void testDelete() {
        when(repo.findById(1L)).thenReturn(Optional.of(tipo));

        assertDoesNotThrow(() -> service.delete(1L));
    }
}
