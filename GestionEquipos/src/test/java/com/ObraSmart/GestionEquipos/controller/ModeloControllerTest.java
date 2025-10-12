package com.ObraSmart.GestionEquipos.controller;

import com.ObraSmart.GestionEquipos.dtos.MarcaDTO;
import com.ObraSmart.GestionEquipos.dtos.ModeloDTO;
import com.ObraSmart.GestionEquipos.service.ModeloService;
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

class ModeloControllerTest {

    @Mock
    private ModeloService modeloService;

    @InjectMocks
    private ModeloController modeloController;

    private ModeloDTO modeloDTO;
    private MarcaDTO marcaDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        marcaDTO = new MarcaDTO();
        marcaDTO.setId(1L);
        marcaDTO.setNombre("Caterpillar");

        modeloDTO = new ModeloDTO();
        modeloDTO.setId(1L);
        modeloDTO.setNombre("Retroexcavadora ZX300");
        modeloDTO.setMarca(marcaDTO);
    }


    @Test
    @DisplayName("Debe devolver lista de modelos con status 200 OK")
    void getAll_ReturnsListAndStatusOk() {
        List<ModeloDTO> modelos = Arrays.asList(modeloDTO);
        when(modeloService.getAll()).thenReturn(modelos);

        ResponseEntity<List<ModeloDTO>> response = modeloController.getAll();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Retroexcavadora ZX300", response.getBody().get(0).getNombre());
        assertEquals("Caterpillar", response.getBody().get(0).getMarca().getNombre());
    }


    @Test
    @DisplayName("Debe devolver un modelo por ID con status 200 OK")
    void getById_ReturnsModeloAndStatusOk() {
        when(modeloService.getById(1L)).thenReturn(modeloDTO);

        ResponseEntity<ModeloDTO> response = modeloController.getById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Retroexcavadora ZX300", response.getBody().getNombre());
        assertEquals("Caterpillar", response.getBody().getMarca().getNombre());
    }


    @Test
    @DisplayName("Debe crear un modelo y devolver status 201 CREATED")
    void create_ReturnsCreatedModeloAndStatusCreated() {
        when(modeloService.create(modeloDTO)).thenReturn(modeloDTO);

        ResponseEntity<ModeloDTO> response = modeloController.create(modeloDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Retroexcavadora ZX300", response.getBody().getNombre());
    }


    @Test
    @DisplayName("Debe actualizar un modelo existente y devolver status 200 OK")
    void update_ReturnsUpdatedModeloAndStatusOk() {
        modeloDTO.setNombre("Retroexcavadora ZX400");
        when(modeloService.update(1L, modeloDTO)).thenReturn(modeloDTO);

        ResponseEntity<ModeloDTO> response = modeloController.update(1L, modeloDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Retroexcavadora ZX400", response.getBody().getNombre());
    }


    @Test
    @DisplayName("Debe eliminar un modelo y devolver status 204 NO_CONTENT")
    void delete_ReturnsNoContentStatus() {
        ResponseEntity<Void> response = modeloController.delete(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}