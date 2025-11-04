package com.ObraSmart.GestionStock.Service.Impl;

import com.ObraSmart.GestionStock.Dto.RepuestoDto;
import com.ObraSmart.GestionStock.Entity.Repuesto;
import com.ObraSmart.GestionStock.Entity.Proveedor;
import com.ObraSmart.GestionStock.Repository.RepuestoRepository;
import com.ObraSmart.GestionStock.Repository.ProveedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RepuestoServiceImplTest {

    @InjectMocks
    private RepuestoServiceImpl service;

    @Mock
    private RepuestoRepository repuestoRepository;

    @Mock
    private ProveedorRepository proveedorRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /* ------------------- GET ------------------- */

    @Test
    void getAllRepuestos_deberiaDevolverListaDto() {
        Repuesto r1 = new Repuesto(); r1.setId(1L); r1.setNombre("R1");
        Repuesto r2 = new Repuesto(); r2.setId(2L); r2.setNombre("R2");

        when(repuestoRepository.findByActivoTrue()).thenReturn(List.of(r1, r2));

        List<RepuestoDto> result = service.getAllRepuestos();

        assertEquals(2, result.size());
        assertEquals("R1", result.get(0).getNombre());
        verify(repuestoRepository).findByActivoTrue();
    }

    @Test
    void getRepuestoById_existente_deberiaDevolverDto() {
        Repuesto r = new Repuesto(); r.setId(1L); r.setNombre("R1");

        when(repuestoRepository.findByIdAndActivoTrue(1L)).thenReturn(Optional.of(r));

        RepuestoDto dto = service.getRepuestoById(1L);

        assertNotNull(dto);
        assertEquals("R1", dto.getNombre());
    }

    @Test
    void getRepuestoById_noExistente_deberiaDevolverNull() {
        when(repuestoRepository.findByIdAndActivoTrue(1L)).thenReturn(Optional.empty());

        RepuestoDto dto = service.getRepuestoById(1L);

        assertNull(dto);
    }

    @Test
    void verificarStockDisponible_noExistente_deberiaDevolverCero() {
        when(repuestoRepository.findByIdAndActivoTrue(1L)).thenReturn(Optional.empty());
        int stock = service.verificarStockDisponible(1L);
        assertEquals(0, stock);
    }

    /* ------------------- SAVE ------------------- */

    @Test
    void saveRepuesto_nombreUnico_deberiaGuardar() {
        RepuestoDto dto = new RepuestoDto();
        dto.setNombre("Nuevo");

        when(repuestoRepository.findByNombreAndActivoTrue("Nuevo")).thenReturn(Optional.empty());
        Repuesto saved = new Repuesto(); saved.setId(1L); saved.setNombre("Nuevo");
        when(repuestoRepository.save(any())).thenReturn(saved);

        RepuestoDto result = service.saveRepuesto(dto);

        assertNotNull(result.getId());
        assertEquals("Nuevo", result.getNombre());
        verify(repuestoRepository).save(any());
    }

    @Test
    void saveRepuesto_nombreDuplicado_deberiaLanzarExcepcion() {
        RepuestoDto dto = new RepuestoDto(); dto.setNombre("R1");
        Repuesto existing = new Repuesto(); existing.setNombre("R1");

        when(repuestoRepository.findByNombreAndActivoTrue("R1")).thenReturn(Optional.of(existing));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.saveRepuesto(dto));
        assertTrue(ex.getMessage().contains("Ya existe un repuesto activo"));
    }

    /* ------------------- UPDATE ------------------- */

    @Test
    void updateRepuesto_existente_deberiaActualizar() {
        Repuesto r = new Repuesto(); r.setId(1L); r.setNombre("Old");
        RepuestoDto dto = new RepuestoDto(); dto.setNombre("New");

        when(repuestoRepository.findByIdAndActivoTrue(1L)).thenReturn(Optional.of(r));
        when(repuestoRepository.findByNombreAndActivoTrue("New")).thenReturn(Optional.empty());
        when(repuestoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        RepuestoDto result = service.updateRepuesto(1L, dto);

        assertEquals("New", result.getNombre());
        verify(repuestoRepository).save(any());
    }

    @Test
    void updateRepuesto_nombreDuplicado_deberiaLanzarExcepcion() {
        Repuesto existingRepuesto = new Repuesto(); existingRepuesto.setId(2L); existingRepuesto.setNombre("Conflicto");
        Repuesto r = new Repuesto(); r.setId(1L); r.setNombre("Old");
        RepuestoDto dto = new RepuestoDto(); dto.setNombre("Conflicto");

        when(repuestoRepository.findByIdAndActivoTrue(1L)).thenReturn(Optional.of(r));
        when(repuestoRepository.findByNombreAndActivoTrue("Conflicto")).thenReturn(Optional.of(existingRepuesto));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.updateRepuesto(1L, dto));
        assertTrue(ex.getMessage().contains("Ya existe otro repuesto activo"));
    }

    /* ------------------- DELETE ------------------- */

    @Test
    void deleteRepuesto_existente_deberiaMarcarInactivo() {
        Repuesto r = new Repuesto(); r.setActivo(true);
        when(repuestoRepository.findByIdAndActivoTrue(1L)).thenReturn(Optional.of(r));
        when(repuestoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.deleteRepuesto(1L);

        assertFalse(r.isActivo());
        verify(repuestoRepository).save(r);
    }

    /* ------------------- STOCK ------------------- */

    @Test
    void sacarDelStock_stockSuficiente_deberiaRestarCantidad() {
        Repuesto r = new Repuesto(); r.setId(1L); r.setNombre("R1"); r.setCantidad(10); r.setStockMinimo(2);
        when(repuestoRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(r));
        when(repuestoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        RepuestoDto result = service.sacarDelStock(1L, 5);

        assertEquals(5, r.getCantidad());
        assertEquals(5, result.getCantidad());
    }

    @Test
    void sacarDelStock_stockInsuficiente_deberiaLanzarExcepcion() {
        Repuesto r = new Repuesto(); r.setCantidad(3);
        when(repuestoRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(r));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.sacarDelStock(1L, 5));
        assertTrue(ex.getMessage().contains("Stock insuficiente"));
    }

    @Test
    void sacarDelStock_cantidadInvalida_deberiaLanzarExcepcion() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.sacarDelStock(1L, 0));
        assertTrue(ex.getMessage().contains("La cantidad a sacar debe ser mayor a 0"));
    }

    @Test
    void agregarAlStock_valido_deberiaIncrementarCantidad() {
        Repuesto r = new Repuesto(); r.setCantidad(5);
        when(repuestoRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(r));
        when(repuestoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        RepuestoDto result = service.agregarAlStock(1L, 10);

        assertEquals(15, r.getCantidad());
        assertEquals(15, result.getCantidad());
    }

    @Test
    void agregarAlStock_cantidadInvalida_deberiaLanzarExcepcion() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.agregarAlStock(1L, 0));
        assertTrue(ex.getMessage().contains("La cantidad a agregar debe ser mayor a 0"));
    }

    @Test
    void getRepuestosStockBajo_deberiaDevolverLista() {
        Repuesto r = new Repuesto(); r.setCantidad(2);
        when(repuestoRepository.findByCantidadLessThanAndActivoTrue(5)).thenReturn(List.of(r));

        List<RepuestoDto> result = service.getRepuestosStockBajo(5);

        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getCantidad());
    }

    /* ------------------- ESTADÍSTICAS ------------------- */

    @Test
    void contarRepuestosActivos_deberiaLlamarRepositorio() {
        when(repuestoRepository.countByActivoTrue()).thenReturn(5L);
        long count = service.contarRepuestosActivos();
        assertEquals(5, count);
    }

    @Test
    void contarAlertasStock_deberiaLlamarRepositorio() {
        when(repuestoRepository.countByCantidadLessThanAndActivoTrue(5)).thenReturn(2L);
        long count = service.contarAlertasStock(5);
        assertEquals(2, count);
    }
}
