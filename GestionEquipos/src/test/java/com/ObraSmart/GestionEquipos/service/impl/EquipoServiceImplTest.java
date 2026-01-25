package com.ObraSmart.GestionEquipos.service.impl;

import com.ObraSmart.GestionEquipos.dtos.EquipoDTO;
import com.ObraSmart.GestionEquipos.entity.*;
import com.ObraSmart.GestionEquipos.exception.BusinessException;
import com.ObraSmart.GestionEquipos.exception.NotFoundException;
import com.ObraSmart.GestionEquipos.repository.EquipoRepository;
import com.ObraSmart.GestionEquipos.repository.MarcaRepository;
import com.ObraSmart.GestionEquipos.repository.ModeloRepository;
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
import static org.mockito.Mockito.*;

class EquipoServiceImplTest {

    @Mock
    private EquipoRepository equipoRepository;

    @Mock
    private TipoEquipoRepository tipoRepo;

    @Mock
    private MarcaRepository marcaRepo;

    @Mock
    private ModeloRepository modeloRepo;

    @InjectMocks
    private EquipoServiceImpl equipoService;

    private TipoEquipo tipoEquipo;
    private Marca marca;
    private Modelo modelo;

    private Equipo equipo;
    private EquipoDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        tipoEquipo = new TipoEquipo();
        tipoEquipo.setId(1L);
        tipoEquipo.setNombre("Retroexcavadora");
        tipoEquipo.setPrefijo("RET");

        marca = new Marca();
        marca.setId(1L);
        marca.setNombre("Caterpillar");

        modelo = new Modelo();
        modelo.setId(1L);
        modelo.setNombre("320D");

        equipo = new Equipo();
        equipo.setId(1L);
        equipo.setNombre("Equipo Test");
        equipo.setNumeroSerie("ABC123");
        equipo.setAnioFabricacion(2020);
        equipo.setPotenciaHp(150.0);
        equipo.setCombustible(Combustible.DIESEL);
        equipo.setEstadoOperativo(Estado_Operativo.DISPONIBLE);
        equipo.setKilometrajeHorasUso(1000.0);
        equipo.setFechaUltimoMantenimiento(LocalDate.now());
        equipo.setProximoMantenimiento(LocalDate.now().plusDays(30));
        equipo.setResponsableMantenimiento("Juan Pérez");
        equipo.setNumeroPatente("AA123BB");
        equipo.setSeguroVigente(true);
        equipo.setFechaVencimientoSeguro(LocalDate.now().plusYears(1));
        equipo.setUbicacionActual("Depósito Central");
        equipo.setActivo(true);
        equipo.setTipoEquipo(tipoEquipo);
        equipo.setMarca(marca);
        equipo.setModelo(modelo);
        equipo.setCodigoInterno("RET0001");

        dto = EquipoDTO.builder()
                .id(1L)
                .nombre("Equipo Test")
                .numeroSerie("ABC123")
                .anioFabricacion(2020)
                .potenciaHp(150.0)
                .combustible(Combustible.DIESEL)
                .estadoOperativo(Estado_Operativo.DISPONIBLE)
                .kilometrajeHorasUso(1000.0)
                .fechaUltimoMantenimiento(LocalDate.now())
                .proximoMantenimiento(LocalDate.now().plusDays(30))
                .responsableMantenimiento("Juan Pérez")
                .seguroVigente(true)
                .fechaVencimientoSeguro(LocalDate.now().plusYears(1))
                .ubicacionActual("Depósito Central")
                .activo(true)
                .idTipoEquipo(1L)
                .idMarca(1L)
                .idModelo(1L)
                .build();
    }

    // ============================================================
    @Test
    @DisplayName("getAll — debe devolver lista de equipos")
    void testGetAll() {
        when(equipoRepository.findAll()).thenReturn(Arrays.asList(equipo));

        List<EquipoDTO> result = equipoService.getAll();

        assertEquals(1, result.size());
        assertEquals("ABC123", result.get(0).getNumeroSerie());
    }

    // ============================================================
    @Test
    @DisplayName("getById — encontrado OK")
    void testGetById_OK() {
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipo));

        EquipoDTO result = equipoService.getById(1L);

        assertNotNull(result);
        assertEquals("ABC123", result.getNumeroSerie());
    }

    @Test
    @DisplayName("getById — debe lanzar NotFoundException")
    void testGetById_NotFound() {
        when(equipoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> equipoService.getById(1L));
    }

    // ============================================================
    @Test
    @DisplayName("create — OK con código interno generado")
    void testCreate_OK() {
        when(tipoRepo.findById(1L)).thenReturn(Optional.of(tipoEquipo));
        when(marcaRepo.findById(1L)).thenReturn(Optional.of(marca));
        when(modeloRepo.findById(1L)).thenReturn(Optional.of(modelo));

        when(equipoRepository.existsByNumeroSerie("ABC123")).thenReturn(false);
        when(equipoRepository.findTopByTipoEquipo_PrefijoOrderByCodigoInternoDesc("RET"))
                .thenReturn(Optional.of(equipo));

        when(equipoRepository.save(any())).thenReturn(equipo);

        EquipoDTO result = equipoService.create(dto);

        assertEquals("RET0001", result.getCodigoInterno());
    }

    @Test
    @DisplayName("create — debe fallar si numeroSerie existe")
    void testCreate_NumeroSerieDuplicado() {
        when(equipoRepository.existsByNumeroSerie("ABC123")).thenReturn(true);

        BusinessException ex =
                assertThrows(BusinessException.class, () -> equipoService.create(dto));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
    }

    // ============================================================
    @Test
    @DisplayName("update — OK actualiza equipo existente")
    void testUpdate_OK() {
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipo));
        when(tipoRepo.findById(1L)).thenReturn(Optional.of(tipoEquipo));
        when(marcaRepo.findById(1L)).thenReturn(Optional.of(marca));
        when(modeloRepo.findById(1L)).thenReturn(Optional.of(modelo));
        when(equipoRepository.save(any())).thenReturn(equipo);

        EquipoDTO result = equipoService.update(1L, dto);

        assertEquals("ABC123", result.getNumeroSerie());
    }

    @Test
    @DisplayName("update — debe lanzar NotFoundException")
    void testUpdate_NotFound() {
        when(equipoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> equipoService.update(1L, dto));
    }

    // ============================================================
    @Test
    @DisplayName("delete — marcado como inactivo")
    void testDelete_OK() {
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipo));

        assertDoesNotThrow(() -> equipoService.delete(1L));
    }

    @Test
    @DisplayName("delete — debe lanzar NotFoundException")
    void testDelete_NotFound() {
        when(equipoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> equipoService.delete(1L));
    }
}
