package com.ObraSmart.GestionReparaciones.Controller;

import com.ObraSmart.GestionReparaciones.Dto.ReparacionDto;
import com.ObraSmart.GestionReparaciones.Dto.ReparacionResponseDto;
import com.ObraSmart.GestionReparaciones.Service.ReparacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ReparacionControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ReparacionService reparacionService;

    @InjectMocks
    private ReparacionController reparacionController;

    private ReparacionResponseDto reparacionResponseDto;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(reparacionController).build();

        reparacionResponseDto = new ReparacionResponseDto();
        reparacionResponseDto.setId(1L);
        reparacionResponseDto.setEquipoId(5L);
        reparacionResponseDto.setDescripcion("Cambio de filtro");
        reparacionResponseDto.setEstado("EN_MANTENIMIENTO");
        reparacionResponseDto.setFechaCreacion(LocalDateTime.now());
    }

    @Test
    void testCrearReparacion_retorna201YJson() throws Exception {
        ReparacionDto dto = new ReparacionDto();
        dto.setEquipoId(5L);
        dto.setDescripcion("Cambio de filtro");

        Mockito.when(reparacionService.crearReparacion(any(ReparacionDto.class)))
                .thenReturn(reparacionResponseDto);

        mockMvc.perform(post("/reparaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.equipoId").value(5L))
                .andExpect(jsonPath("$.descripcion").value("Cambio de filtro"))
                .andExpect(jsonPath("$.estado").value("EN_MANTENIMIENTO"));
    }

    @Test
    void testObtenerReparacionPorId_existente() throws Exception {
        Mockito.when(reparacionService.getById(1L)).thenReturn(reparacionResponseDto);

        mockMvc.perform(get("/reparaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.descripcion").value("Cambio de filtro"))
                .andExpect(jsonPath("$.estado").value("EN_MANTENIMIENTO"));
    }
}
