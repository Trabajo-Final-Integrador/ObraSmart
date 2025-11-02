package com.ObraSmart.GestionStock.Controller;

import com.ObraSmart.GestionStock.Dto.ProveedorDto;
import com.ObraSmart.GestionStock.Entity.Proveedor;
import com.ObraSmart.GestionStock.Repository.ProveedorRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ProveedorControllerTest {

    @Mock
    private ProveedorRepository proveedorRepository;

    @InjectMocks
    private ProveedorController controller;

    private Proveedor proveedor;
    private ProveedorDto proveedorDto;
    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        proveedor = Proveedor.builder()
                .id(1L)
                .nombre("Proveedor Test")
                .contacto("Juan Pérez")
                .telefono("123456")
                .email("test@correo.com")
                .direccion("Calle 123")
                .identificador("CUIT-2030")
                .build();

        proveedorDto = ProveedorDto.builder()
                .id(1L)
                .nombre("Proveedor Test")
                .contacto("Juan Pérez")
                .telefono("123456")
                .email("test@correo.com")
                .direccion("Calle 123")
                .identificador("CUIT-2030")
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close(); // ✅ cierra correctamente los mocks
    }

    @Test
    void testGetAll() {
        when(proveedorRepository.findAll()).thenReturn(List.of(proveedor));

        ResponseEntity<List<ProveedorDto>> response = controller.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Proveedor Test", response.getBody().get(0).getNombre());
    }

    @Test
    void testGetById_found() {
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));

        ResponseEntity<ProveedorDto> response = controller.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Proveedor Test", response.getBody().getNombre());
    }

    @Test
    void testGetById_notFound() {
        when(proveedorRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<ProveedorDto> response = controller.getById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testCreate() {
        when(proveedorRepository.save(any(Proveedor.class))).thenReturn(proveedor);

        ResponseEntity<ProveedorDto> response = controller.create(proveedorDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Proveedor Test", response.getBody().getNombre());
        verify(proveedorRepository, times(1)).save(any(Proveedor.class));
    }

    @Test
    void testUpdate_found() {
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(proveedorRepository.save(any(Proveedor.class))).thenReturn(proveedor);

        ResponseEntity<ProveedorDto> response = controller.update(1L, proveedorDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Proveedor Test", response.getBody().getNombre());
    }

    @Test
    void testUpdate_notFound() {
        when(proveedorRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<ProveedorDto> response = controller.update(1L, proveedorDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(proveedorRepository, never()).save(any());
    }

    @Test
    void testDelete_found() {
        when(proveedorRepository.existsById(1L)).thenReturn(true);
        doNothing().when(proveedorRepository).deleteById(1L);

        ResponseEntity<Void> response = controller.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(proveedorRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_notFound() {
        when(proveedorRepository.existsById(1L)).thenReturn(false);

        ResponseEntity<Void> response = controller.delete(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(proveedorRepository, never()).deleteById(anyLong());
    }
}
