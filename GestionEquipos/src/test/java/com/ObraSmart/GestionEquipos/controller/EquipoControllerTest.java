package com.ObraSmart.GestionEquipos.controller;

import com.ObraSmart.GestionEquipos.dtos.EquipoDTO;
import com.ObraSmart.GestionEquipos.service.EquipoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EquipoControllerTest {

    @Mock
    private EquipoService equipoService;

    @InjectMocks
    private EquipoController equipoController;

    private EquipoDTO equipoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        equipoDTO = new EquipoDTO();
        equipoDTO.setId(1L);
        equipoDTO.setNombre("Retro 320D");
        equipoDTO.setNumeroSerie("ABC123");
        equipoDTO.setActivo(true);
    }

    // ============================================================
    @Test
    @DisplayName("GET /equipos → debe devolver lista con status 200")
    void testGetAllEquipos() {
        when(equipoService.getAll()).thenReturn(Arrays.asList(equipoDTO));

        ResponseEntity<List<EquipoDTO>> response = equipoController.getAllEquipos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(equipoService, times(1)).getAll();
    }

    // ============================================================
    @Test
    @DisplayName("GET /equipos/{id} → debe devolver un equipo con status 200")
    void testGetEquipoById() {
        when(equipoService.getById(1L)).thenReturn(equipoDTO);

        ResponseEntity<EquipoDTO> response = equipoController.getEquipoById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ABC123", response.getBody().getNumeroSerie());
        verify(equipoService).getById(1L);
    }

    // ============================================================
    @Test
    @DisplayName("POST /equipos → debe crear equipo con status 201")
    void testCreateEquipo() {
        when(equipoService.create(equipoDTO)).thenReturn(equipoDTO);

        ResponseEntity<EquipoDTO> response = equipoController.createEquipo(equipoDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("ABC123", response.getBody().getNumeroSerie());
        verify(equipoService).create(equipoDTO);
    }

    // ============================================================
    @Test
    @DisplayName("PUT /equipos/{id} → debe actualizar y devolver 200")
    void testUpdateEquipo() {
        when(equipoService.update(1L, equipoDTO)).thenReturn(equipoDTO);

        ResponseEntity<EquipoDTO> response = equipoController.updateEquipo(1L, equipoDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(equipoService).update(1L, equipoDTO);
    }

    // ============================================================
    @Test
    @DisplayName("DELETE /equipos/{id} → debe devolver 204")
    void testDeleteEquipo() {
        ResponseEntity<Void> response = equipoController.deleteEquipo(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(equipoService).delete(1L);
    }
}
