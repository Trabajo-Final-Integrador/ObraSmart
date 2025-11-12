package com.ObraSmart.GestionEquipos.controller;

import com.ObraSmart.GestionEquipos.dtos.TipoEquipoDTO;
import com.ObraSmart.GestionEquipos.service.TipoEquipoService;
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
import static org.mockito.Mockito.when;

class TipoEquipoControllerTest {

    @Mock
    private TipoEquipoService tipoEquipoService;

    @InjectMocks
    private TipoEquipoController tipoEquipoController;

    private TipoEquipoDTO tipoEquipoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        tipoEquipoDTO = new TipoEquipoDTO();
        tipoEquipoDTO.setId(1L);
        tipoEquipoDTO.setNombre("Excavadora");
    }


    @Test
    @DisplayName("Debe devolver lista de tipos de equipo con status 200 OK")
    void getAll_ReturnsListAndStatusOk() {
        List<TipoEquipoDTO> tipos = Arrays.asList(tipoEquipoDTO);
        when(tipoEquipoService.getAll()).thenReturn(tipos);

        ResponseEntity<List<TipoEquipoDTO>> response = tipoEquipoController.getAll();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Excavadora", response.getBody().get(0).getNombre());
    }


    @Test
    @DisplayName("Debe devolver un tipo de equipo por ID con status 200 OK")
    void getById_ReturnsTipoEquipoAndStatusOk() {
        when(tipoEquipoService.getById(1L)).thenReturn(tipoEquipoDTO);

        ResponseEntity<TipoEquipoDTO> response = tipoEquipoController.getById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Excavadora", response.getBody().getNombre());
        assertEquals(1L, response.getBody().getId());
    }


    @Test
    @DisplayName("Debe crear un tipo de equipo y devolver status 201 CREATED")
    void create_ReturnsCreatedTipoEquipoAndStatusCreated() {
        when(tipoEquipoService.create(tipoEquipoDTO)).thenReturn(tipoEquipoDTO);

        ResponseEntity<TipoEquipoDTO> response = tipoEquipoController.create(tipoEquipoDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Excavadora", response.getBody().getNombre());
    }


    @Test
    @DisplayName("Debe actualizar un tipo de equipo existente y devolver status 200 OK")
    void update_ReturnsUpdatedTipoEquipoAndStatusOk() {
        tipoEquipoDTO.setNombre("Retroexcavadora");
        when(tipoEquipoService.update(1L, tipoEquipoDTO)).thenReturn(tipoEquipoDTO);

        ResponseEntity<TipoEquipoDTO> response = tipoEquipoController.update(1L, tipoEquipoDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Retroexcavadora", response.getBody().getNombre());
    }


    @Test
    @DisplayName("Debe eliminar un tipo de equipo y devolver status 204 NO_CONTENT")
    void delete_ReturnsNoContentStatus() {
        ResponseEntity<Void> response = tipoEquipoController.delete(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}