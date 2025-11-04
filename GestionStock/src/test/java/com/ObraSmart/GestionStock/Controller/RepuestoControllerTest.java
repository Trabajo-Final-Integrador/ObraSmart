package com.ObraSmart.GestionStock.Controller;

import com.ObraSmart.GestionStock.Dto.RepuestoDto;
import com.ObraSmart.GestionStock.Service.RepuestoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RepuestoControllerTest {

    @InjectMocks
    private RepuestoController controller;

    @Mock
    private RepuestoService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listar_deberiaDevolverListaDeRepuestos() {
        RepuestoDto r1 = new RepuestoDto();
        r1.setId(1L);
        r1.setNombre("Repuesto1");
        RepuestoDto r2 = new RepuestoDto();
        r2.setId(2L);
        r2.setNombre("Repuesto2");

        when(service.getAllRepuestos()).thenReturn(Arrays.asList(r1, r2));

        ResponseEntity<List<RepuestoDto>> response = controller.listar();

        assertEquals(2, response.getBody().size());
        verify(service).getAllRepuestos();
    }

    @Test
    void getById_existente_deberiaDevolverRepuesto() {
        RepuestoDto r = new RepuestoDto();
        r.setId(1L);
        r.setNombre("Repuesto1");

        when(service.getRepuestoById(1L)).thenReturn(r);

        ResponseEntity<RepuestoDto> response = controller.getById(1L);

        assertEquals(r, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void getById_noExistente_deberiaDevolverNotFound() {
        when(service.getRepuestoById(1L)).thenReturn(null);

        ResponseEntity<RepuestoDto> response = controller.getById(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void crear_deberiaDevolverRepuestoCreado() {
        RepuestoDto r = new RepuestoDto();
        r.setNombre("Nuevo");

        RepuestoDto rCreado = new RepuestoDto();
        rCreado.setId(1L);
        rCreado.setNombre("Nuevo");

        when(service.saveRepuesto(r)).thenReturn(rCreado);

        ResponseEntity<RepuestoDto> response = controller.crear(r);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(rCreado, response.getBody());
    }

    @Test
    void actualizar_existente_deberiaDevolverRepuestoActualizado() {
        RepuestoDto r = new RepuestoDto();
        r.setNombre("Actualizado");

        RepuestoDto rActualizado = new RepuestoDto();
        rActualizado.setId(1L);
        rActualizado.setNombre("Actualizado");

        when(service.updateRepuesto(1L, r)).thenReturn(rActualizado);

        ResponseEntity<RepuestoDto> response = controller.actualizar(1L, r);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(rActualizado, response.getBody());
    }

    @Test
    void actualizar_noExistente_deberiaDevolverNotFound() {
        RepuestoDto r = new RepuestoDto();
        when(service.updateRepuesto(1L, r)).thenReturn(null);

        ResponseEntity<RepuestoDto> response = controller.actualizar(1L, r);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void eliminar_deberiaInvocarServicio() {
        ResponseEntity<Void> response = controller.eliminar(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(service).deleteRepuesto(1L);
    }

    @Test
    void sacar_stock_valido_deberiaDevolverRepuestoActualizado() {
        RepuestoController.CantidadDto cantidad = new RepuestoController.CantidadDto();
        cantidad.setCantidad(5);

        RepuestoDto actualizado = new RepuestoDto();
        actualizado.setId(1L);
        actualizado.setNombre("Repuesto1");

        when(service.sacarDelStock(1L, 5)).thenReturn(actualizado);

        ResponseEntity<?> response = controller.sacar(1L, cantidad);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(actualizado, response.getBody());
    }

    @Test
    void sacar_stock_error_deberiaDevolverConflict() {
        RepuestoController.CantidadDto cantidad = new RepuestoController.CantidadDto();
        cantidad.setCantidad(10);

        when(service.sacarDelStock(1L, 10)).thenThrow(new RuntimeException("Stock insuficiente"));

        ResponseEntity<?> response = controller.sacar(1L, cantidad);

        assertEquals(409, response.getStatusCodeValue());
        assertEquals("Stock insuficiente", response.getBody());
    }

    @Test
    void stockBajo_deberiaDevolverLista() {
        RepuestoDto r1 = new RepuestoDto();
        r1.setId(1L);
        r1.setNombre("Bajo1");

        when(service.getRepuestosStockBajo(5)).thenReturn(List.of(r1));

        ResponseEntity<List<RepuestoDto>> response = controller.stockBajo(5);

        assertEquals(1, response.getBody().size());
        verify(service).getRepuestosStockBajo(5);
    }
}
