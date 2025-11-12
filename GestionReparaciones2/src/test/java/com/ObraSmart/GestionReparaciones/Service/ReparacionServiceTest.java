package com.ObraSmart.GestionReparaciones.Service;

import com.ObraSmart.GestionReparaciones.Client.EquipoClient;
import com.ObraSmart.GestionReparaciones.Dto.ReparacionDto;
import com.ObraSmart.GestionReparaciones.Dto.ReparacionResponseDto;
import com.ObraSmart.GestionReparaciones.Entity.EstadoReparacion;
import com.ObraSmart.GestionReparaciones.Entity.Reparacion;
import com.ObraSmart.GestionReparaciones.Repository.ReparacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios del servicio de Reparaciones.
 * Verifica la creación, obtención y manejo de errores.
 */
class ReparacionServiceTest {

    @InjectMocks
    private ReparacionService reparacionService; // Clase que testeamos

    @Mock
    private ReparacionRepository repo;

    @Mock
    private EquipoClient equipoClient;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearReparacion_guardadaYSaneaCampos() {
        // Preparar DTO de entrada
        ReparacionDto dto = new ReparacionDto();
        dto.setEquipoId(1L);
        dto.setDescripcion("Avería prueba");

        // Simular validación del equipo (mock simple)
        when(equipoClient.getEquipoById(1L)).thenReturn(null);

        // Configurar comportamiento del repo al guardar
        ArgumentCaptor<Reparacion> capt = ArgumentCaptor.forClass(Reparacion.class);
        Reparacion saved = new Reparacion();
        saved.setId(100L);
        saved.setEquipoId(1L);
        saved.setDescripcion("Avería prueba");
        saved.setEstado(EstadoReparacion.DISPONIBLE); // 👈 reemplaza PENDIENTE
        saved.setFechaCreacion(LocalDateTime.now());

        when(repo.save(any(Reparacion.class))).thenReturn(saved);

        // Ejecutar método
        ReparacionResponseDto resp = reparacionService.crearReparacion(dto);

        // Verificar resultados
        assertNotNull(resp);
        assertEquals(100L, resp.getId());
        assertEquals(1L, resp.getEquipoId());
        assertEquals("Avería prueba", resp.getDescripcion());
        assertEquals(EstadoReparacion.DISPONIBLE.name(), resp.getEstado()); // 👈 enum corregido

        // Capturar y verificar objeto pasado al repositorio
        verify(repo).save(capt.capture());
        Reparacion passed = capt.getValue();
        assertEquals(1L, passed.getEquipoId());
        assertNotNull(passed.getFechaCreacion());
    }

    @Test
    void obtenerPorId_existente_devuelveResponse() {
        // Reparación simulada en base de datos
        Reparacion r = new Reparacion();
        r.setId(200L);
        r.setEquipoId(2L);
        r.setDescripcion("Chequeo general");
        r.setEstado(EstadoReparacion.EN_MANTENIMIENTO); // 👈 reemplaza EN_CURSO

        when(repo.findById(200L)).thenReturn(Optional.of(r));

        // Ejecutar
        ReparacionResponseDto resp = reparacionService.obtenerPorId(200L);

        // Verificar
        assertNotNull(resp);
        assertEquals(200L, resp.getId());
        assertEquals("EN_MANTENIMIENTO", resp.getEstado()); // 👈 enum válido
    }

    @Test
    void obtenerPorId_noExiste_lanzaExcepcion() {
        when(repo.findById(999L)).thenReturn(Optional.empty());

        // Ajustar el tipo de excepción si tu servicio usa una personalizada
        assertThrows(RuntimeException.class, () -> reparacionService.obtenerPorId(999L));
    }
}
