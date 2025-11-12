package com.ObraSmart.GestionEquipos.controller;

import com.ObraSmart.GestionEquipos.dtos.EquipoDTO;
import com.ObraSmart.GestionEquipos.entity.Estado_Operativo;
import com.ObraSmart.GestionEquipos.service.EquipoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
        equipoDTO.setNumeroSerie("ABC123");
        equipoDTO.setNumeroPatente("ZZZ999");
        equipoDTO.setEstadoOperativo(Estado_Operativo.valueOf("DISPONIBLE"));
        equipoDTO.setKilometrajeHorasUso(1200.0);
        equipoDTO.setFechaUltimoMantenimiento(LocalDate.now());
        equipoDTO.setProximoMantenimiento(LocalDate.now().plusDays(30));
        equipoDTO.setResponsableMantenimiento("Juan Pérez");
        equipoDTO.setSeguroVigente(true);
        equipoDTO.setFechaVencimientoSeguro(LocalDate.now().plusYears(1));
        equipoDTO.setUbicacionActual("Depósito Central");
        equipoDTO.setActivo(true);
    }

    // ------------------------------------------------------------
    @Test
    @DisplayName("Debe devolver lista de equipos con status 200 OK")
    void getAllEquipos_ReturnsListAndStatusOk() {
        List<EquipoDTO> equipos = Arrays.asList(equipoDTO);
        when(equipoService.getAll()).thenReturn(equipos);

        ResponseEntity<List<EquipoDTO>> response = equipoController.getAllEquipos();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("ABC123", response.getBody().get(0).getNumeroSerie());
    }

    // ------------------------------------------------------------
    @Test
    @DisplayName("Debe devolver un equipo por ID con status 200 OK")
    void getEquipoById_ReturnsEquipoAndStatusOk() {
        when(equipoService.getById(1L)).thenReturn(equipoDTO);

        ResponseEntity<EquipoDTO> response = equipoController.getEquipoById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ABC123", response.getBody().getNumeroSerie());
    }

    // ------------------------------------------------------------
    @Test
    @DisplayName("Debe crear un equipo y devolver status 201 CREATED")
    void createEquipo_ReturnsCreatedStatus() {
        when(equipoService.create(equipoDTO)).thenReturn(equipoDTO);

        ResponseEntity<EquipoDTO> response = equipoController.createEquipo(equipoDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("ABC123", response.getBody().getNumeroSerie());
    }

    // ------------------------------------------------------------
    @Test
    @DisplayName("Debe actualizar un equipo existente y devolver status 200 OK")
    void updateEquipo_ReturnsUpdatedEquipoAndStatusOk() {
        equipoDTO.setNumeroSerie("NEW123");
        when(equipoService.update(1L, equipoDTO)).thenReturn(equipoDTO);

        ResponseEntity<EquipoDTO> response = equipoController.updateEquipo(1L, equipoDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("NEW123", response.getBody().getNumeroSerie());
    }

    // ------------------------------------------------------------
    @Test
    @DisplayName("Debe eliminar un equipo y devolver status 204 No Content")
    void deleteEquipo_ReturnsNoContentStatus() {
        ResponseEntity<Void> response = equipoController.deleteEquipo(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}