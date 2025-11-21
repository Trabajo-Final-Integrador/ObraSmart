package com.ObraSmart.GestionReparaciones.Controller;

import com.ObraSmart.GestionReparaciones.Dto.ReparacionRequestDto;
import com.ObraSmart.GestionReparaciones.Dto.ReparacionResponseDto;
import com.ObraSmart.GestionReparaciones.Entity.EstadoReparacion;
import com.ObraSmart.GestionReparaciones.Service.ReparacionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReparacionController.class)
class ReparacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReparacionService reparacionService;

    private ReparacionResponseDto response;

    @BeforeEach
    void setup() {
        response = new ReparacionResponseDto();
        response.setId(1L);
        response.setEquipoId(10L);
        response.setDescripcion("Cambio de aceite");
        response.setEstadoReparacion(EstadoReparacion.CREADA);
        response.setResponsableId(1L);
        response.setResponsableNombreCompleto("Usuario Test");
    }

    @Test
    @DisplayName("POST /api/reparaciones debe crear una reparación")
    void testCrearReparacion() throws Exception {

        when(reparacionService.crearReparacion(any(), anyLong()))
                .thenReturn(response);

        // JSON CORRECTO (antes faltaban campos obligatorios → 400)
        String json = """
                {
                    "equipoId": 10,
                    "descripcion": "Cambio de aceite",
                    "tipoMantenimiento": "PREVENTIVO",
                    "responsableId": 1,
                    "responsableNombreCompleto": "Usuario Test",
                    "direccion": "Córdoba, Argentina"
                }
                """;

        mockMvc.perform(post("/api/reparaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.estadoReparacion").value("CREADA"));
    }

    @Test
    @DisplayName("GET /api/reparaciones/{id} debe devolver reparación")
    void testObtenerPorId() throws Exception {

        when(reparacionService.obtenerPorId(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/reparaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("GET /api/reparaciones debe listar todas")
    void testListarTodas() throws Exception {

        when(reparacionService.listarTodas()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/reparaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @DisplayName("PATCH /api/reparaciones/{id}/estado debe cambiar estado")
    void testCambiarEstado() throws Exception {

        response.setEstadoReparacion(EstadoReparacion.EN_PROCESO);

        when(reparacionService.cambiarEstado(
                eq(1L),
                eq(EstadoReparacion.EN_PROCESO),
                anyLong(),
                any()
        )).thenReturn(response);

        mockMvc.perform(patch("/api/reparaciones/1/estado")
                        .param("nuevoEstado", "EN_PROCESO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoReparacion").value("EN_PROCESO"));
    }
}
