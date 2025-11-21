package com.ObraSmart.GestionEquipos.controller;

import com.ObraSmart.GestionEquipos.dtos.MarcaDTO;
import com.ObraSmart.GestionEquipos.service.MarcaService;
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

class MarcaControllerTest {

    @Mock
    private MarcaService marcaService;

    @InjectMocks
    private MarcaController marcaController;

    private MarcaDTO marcaDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        marcaDTO = new MarcaDTO();
        marcaDTO.setId(1L);
        marcaDTO.setNombre("Caterpillar");
    }

    @Test
    @DisplayName("Debe devolver lista de marcas con status 200 OK")
    void getAll_ReturnsListAndStatusOk() {
        List<MarcaDTO> marcas = Arrays.asList(marcaDTO);
        when(marcaService.getAll()).thenReturn(marcas);

        ResponseEntity<List<MarcaDTO>> response = marcaController.getAll();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Caterpillar", response.getBody().get(0).getNombre());
    }

    @Test
    @DisplayName("Debe devolver una marca por ID con status 200 OK")
    void getById_ReturnsMarcaAndStatusOk() {
        when(marcaService.getById(1L)).thenReturn(marcaDTO);

        ResponseEntity<MarcaDTO> response = marcaController.getById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Caterpillar", response.getBody().getNombre());
        assertEquals(1L, response.getBody().getId());
    }


    @Test
    @DisplayName("Debe crear una marca y devolver status 201 CREATED")
    void create_ReturnsCreatedMarcaAndStatusCreated() {
        when(marcaService.create(marcaDTO)).thenReturn(marcaDTO);

        ResponseEntity<MarcaDTO> response = marcaController.create(marcaDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Caterpillar", response.getBody().getNombre());
    }


    @Test
    @DisplayName("Debe actualizar una marca existente y devolver status 200 OK")
    void update_ReturnsUpdatedMarcaAndStatusOk() {
        marcaDTO.setNombre("John Deere");
        when(marcaService.update(1L, marcaDTO)).thenReturn(marcaDTO);

        ResponseEntity<MarcaDTO> response = marcaController.update(1L, marcaDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("John Deere", response.getBody().getNombre());
    }


    @Test
    @DisplayName("Debe eliminar una marca y devolver status 204 NO_CONTENT")
    void delete_ReturnsNoContentStatus() {
        ResponseEntity<Void> response = marcaController.delete(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}