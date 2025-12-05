package com.ObraSmart.GestionReparaciones.service.impl;

import com.ObraSmart.GestionReparaciones.client.EquipoClient;
import com.ObraSmart.GestionReparaciones.dto.ReparacionRequestDto;
import com.ObraSmart.GestionReparaciones.dto.ReparacionResponseDto;
import com.ObraSmart.GestionReparaciones.entity.EstadoReparacion;
import com.ObraSmart.GestionReparaciones.entity.Reparacion;
import com.ObraSmart.GestionReparaciones.repository.ReparacionEstadoHistorialRepository;
import com.ObraSmart.GestionReparaciones.repository.ReparacionRepository;
import com.ObraSmart.GestionReparaciones.service.GeocodingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReparacionServiceImplTest {

    @Mock
    private ReparacionRepository reparacionRepository;
    @Mock
    private ReparacionEstadoHistorialRepository historialRepo;
    @Mock
    private EquipoClient equipoClient;
    @Mock
    private GeocodingService geocodingService;

    @InjectMocks
    private ReparacionServiceImpl reparacionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ============= TEST CREAR =============
    @Test
    void testCrearReparacion() {
        ReparacionRequestDto req = new ReparacionRequestDto();
        req.setEquipoId(1L);
        req.setDescripcion("Cambio de aceite");

        Reparacion saved = new Reparacion();
        saved.setId(10L);
        saved.setEquipoId(1L);
        saved.setEstadoReparacion(EstadoReparacion.CREADA);

        when(reparacionRepository.save(any())).thenReturn(saved);

        ReparacionResponseDto result =
                reparacionService.crearReparacion(req, 5L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        verify(equipoClient).validarEquipoExiste(1L);
    }

    // ============= TEST OBTENER =============
    @Test
    void testObtenerPorId() {
        Reparacion rep = new Reparacion();
        rep.setId(3L);
        rep.setDescripcion("Filtro");

        when(reparacionRepository.findById(3L))
                .thenReturn(Optional.of(rep));

        ReparacionResponseDto dto = reparacionService.obtenerPorId(3L);
        assertEquals(3L, dto.getId());
    }

    // ============= TEST CAMBIAR ESTADO =============
    @Test
    void testCambiarEstado() {
        Reparacion r = new Reparacion();
        r.setId(1L);
        r.setEquipoId(20L);
        r.setEstadoReparacion(EstadoReparacion.CREADA);

        when(reparacionRepository.findById(1L))
                .thenReturn(Optional.of(r));

        ReparacionResponseDto res =
                reparacionService.cambiarEstado(1L, EstadoReparacion.EN_PROCESO, 10L, "Inicio");

        assertEquals(EstadoReparacion.EN_PROCESO, res.getEstadoReparacion());
        verify(reparacionRepository).save(any());
    }
}
