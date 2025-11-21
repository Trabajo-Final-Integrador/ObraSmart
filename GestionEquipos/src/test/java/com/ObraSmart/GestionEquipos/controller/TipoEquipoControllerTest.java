package com.ObraSmart.GestionEquipos.controller;

import com.ObraSmart.GestionEquipos.dtos.TipoEquipoDTO;
import com.ObraSmart.GestionEquipos.service.TipoEquipoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TipoEquipoControllerTest {

    @Mock
    private TipoEquipoService tipoEquipoService;

    @InjectMocks
    private TipoEquipoController tipoEquipoController;

    private TipoEquipoDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dto = TipoEquipoDTO.builder()
                .id(1L)
                .nombre("Retroexcavadora")
                .prefijo("RET")
                .build();
    }

    // ============================================================
    @Test
    @DisplayName("POST /api/tipos-equipo → debe crear tipo")
    void testCreate() {
        when(tipoEquipoService.create(dto)).thenReturn(dto);

        ResponseEntity<TipoEquipoDTO> response = tipoEquipoController.create(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("RET", response.getBody().getPrefijo());
        verify(tipoEquipoService).create(dto);
    }

    // ============================================================
    @Test
    @DisplayName("PUT /api/tipos-equipo/{id} → debe actualizar tipo")
    void testUpdate() {
        when(tipoEquipoService.update(1L, dto)).thenReturn(dto);

        ResponseEntity<TipoEquipoDTO> response = tipoEquipoController.update(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(tipoEquipoService).update(1L, dto);
    }

    // ============================================================
    @Test
    @DisplayName("DELETE /api/tipos-equipo/{id} → debe devolver 204")
    void testDelete() {
        ResponseEntity<Void> response = tipoEquipoController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(tipoEquipoService).delete(1L);
    }

    // ============================================================
    @Test
    @DisplayName("GET /api/tipos-equipo/{id} → debe devolver tipo")
    void testGetById() {
        when(tipoEquipoService.getById(1L)).thenReturn(dto);

        ResponseEntity<TipoEquipoDTO> response = tipoEquipoController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("RET", response.getBody().getPrefijo());
        verify(tipoEquipoService).getById(1L);
    }

    // ============================================================
    @Test
    @DisplayName("GET /api/tipos-equipo → debe listar todos")
    void testGetAll() {
        when(tipoEquipoService.getAll()).thenReturn(Arrays.asList(dto));

        ResponseEntity<List<TipoEquipoDTO>> response = tipoEquipoController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(tipoEquipoService).getAll();
    }
}
