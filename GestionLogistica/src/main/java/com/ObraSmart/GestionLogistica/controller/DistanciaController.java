package com.ObraSmart.GestionLogistica.controller;


import com.ObraSmart.GestionLogistica.dto.DistanciaCalculoDto;
import com.ObraSmart.GestionLogistica.service.impl.DistanciaService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/distancias")
@RequiredArgsConstructor
@Tag(name = "Distancias", description = "Endpoints para calcular distancias entre equipos y obradores")
public class DistanciaController {

    private final DistanciaService distanciaService;

    @GetMapping("/calcular")
    @Operation(
            summary = "Calcular distancia entre equipo y obrador",
            description = "Calcula la distancia en kilómetros entre un equipo y un obrador usando sus coordenadas GPS y la fórmula de Haversine"
    )
    public DistanciaCalculoDto calcularDistancia(
            @Parameter(description = "ID del equipo", required = true, example = "1")
            @RequestParam Long equipoId,

            @Parameter(description = "ID del obrador", required = true, example = "1")
            @RequestParam Long obradorId
    ) {
        return distanciaService.calcularDistancia(equipoId, obradorId);
    }
}