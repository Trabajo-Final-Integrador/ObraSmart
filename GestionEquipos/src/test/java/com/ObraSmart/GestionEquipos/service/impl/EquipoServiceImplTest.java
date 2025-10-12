package com.ObraSmart.GestionEquipos.service.impl;

import com.ObraSmart.GestionEquipos.dtos.EquipoDTO;
import com.ObraSmart.GestionEquipos.entity.Equipo;
import com.ObraSmart.GestionEquipos.entity.Estado_Operativo;
import com.ObraSmart.GestionEquipos.exception.BusinessException;
import com.ObraSmart.GestionEquipos.exception.NotFoundException;
import com.ObraSmart.GestionEquipos.mapers.EquipoMapers;
import com.ObraSmart.GestionEquipos.repository.EquipoRepository;
import com.ObraSmart.GestionEquipos.repository.TipoEquipoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class EquipoServiceImplTest {

    @Mock
    private EquipoRepository equipoRepository;

    @Mock
    private TipoEquipoRepository tipoEquipoRepository;

    @InjectMocks
    private EquipoServiceImpl equipoService;

    private Equipo equipo;
    private EquipoDTO equipoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        equipo = new Equipo();
        equipo.setId(1L);
        equipo.setNumeroSerie("ABC123");
        equipo.setNumeroPatente("ZZZ999");
        equipo.setActivo(true);

        equipoDTO = new EquipoDTO();
        equipoDTO.setId(1L);
        equipoDTO.setNumeroSerie("ABC123");
        equipoDTO.setNumeroPatente("ZZZ999");
        equipoDTO.setActivo(true);
    }

    @Test
    void getAll() {
        when(equipoRepository.findAll()).thenReturn(Arrays.asList(equipo));

        List<EquipoDTO> result = equipoService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ABC123", result.get(0).getNumeroSerie());
    }

    @Test
    void getById() {
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipo));

        EquipoDTO result = equipoService.getById(1L);

        assertNotNull(result);
        assertEquals("ABC123", result.getNumeroSerie());
        assertEquals("ZZZ999", result.getNumeroPatente());
    }

    @Test
    @DisplayName("Debe lanzar NotFoundException si no existe el equipo")
    void getById_ThrowsException_WhenNotFound() {
        when(equipoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> equipoService.getById(1L));
    }

    @Test
    void create() {
        when(equipoRepository.existsByNumeroSerie("ABC123")).thenReturn(false);
        when(equipoRepository.existsByNumeroPatente("ZZZ999")).thenReturn(false);
        when(equipoRepository.save(EquipoMapers.toEntity(equipoDTO))).thenReturn(equipo);

        EquipoDTO result = equipoService.create(equipoDTO);

        assertNotNull(result);
        assertEquals("ABC123", result.getNumeroSerie());
        assertEquals("ZZZ999", result.getNumeroPatente());
    }

    @Test
    @DisplayName("Debe lanzar BusinessException si el número de serie está duplicado")
    void create_ThrowsException_WhenNumeroSerieDuplicado() {
        when(equipoRepository.existsByNumeroSerie("ABC123")).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> equipoService.create(equipoDTO));
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
    }

    @Test
    @DisplayName("Debe lanzar BusinessException si la patente está duplicada")
    void create_ThrowsException_WhenPatenteDuplicada() {
        equipoDTO.setNumeroPatente("ZZZ999");

        when(equipoRepository.existsByNumeroSerie("ABC123")).thenReturn(false);
        when(equipoRepository.existsByNumeroPatente("ZZZ999")).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> equipoService.create(equipoDTO));
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
    }

    @Test
    void update() {
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipo));
        when(equipoRepository.existsByNumeroSerie("ABC123")).thenReturn(false);
        when(equipoRepository.existsByNumeroPatente("ZZZ999")).thenReturn(false);
        when(equipoRepository.save(equipo)).thenReturn(equipo);

        equipoDTO.setEstadoOperativo(Estado_Operativo.valueOf("DISPONIBLE"));
        equipoDTO.setKilometrajeHorasUso(1200.0);
        equipoDTO.setFechaUltimoMantenimiento(LocalDate.now());
        equipoDTO.setProximoMantenimiento(LocalDate.now().plusDays(30));
        equipoDTO.setResponsableMantenimiento("Juan Pérez");
        equipoDTO.setSeguroVigente(true);
        equipoDTO.setFechaVencimientoSeguro(LocalDate.now().plusYears(1));
        equipoDTO.setUbicacionActual("Depósito Central");
        equipoDTO.setActivo(true);

        EquipoDTO result = equipoService.update(1L, equipoDTO);

        assertNotNull(result);
        assertEquals("ABC123", result.getNumeroSerie());
        assertEquals("ZZZ999", result.getNumeroPatente());


    }
    @Test
    @DisplayName("Debe lanzar NotFoundException si no existe el equipo al actualizar")
    void update_ThrowsException_WhenEquipoNotFound() {
        when(equipoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> equipoService.update(1L, equipoDTO));
    }

    @Test
    void delete() {
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipo));
        when(equipoRepository.save(equipo)).thenReturn(equipo);

        assertDoesNotThrow(() -> equipoService.delete(1L));
        assertFalse(equipo.getActivo());
    }

    @Test
    @DisplayName("Debe lanzar NotFoundException al intentar eliminar un equipo inexistente")
    void delete_ThrowsException_WhenNotFound() {
        when(equipoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> equipoService.delete(1L));
    }
}