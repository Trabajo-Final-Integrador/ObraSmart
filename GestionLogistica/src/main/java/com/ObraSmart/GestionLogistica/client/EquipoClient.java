package com.ObraSmart.GestionLogistica.client;

import com.ObraSmart.GestionLogistica.dto.EquipoSimpleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
@Slf4j
public class EquipoClient {

    private final RestTemplate restTemplate;

    @Value("${microservices.equipos.url:http://localhost:8081}")
    private String equiposServiceUrl;

    /**
     * Obtiene la información de un equipo por su ID
     * @param equipoId ID del equipo
     * @return Información del equipo con coordenadas
     */
    public EquipoSimpleDto obtenerEquipo(Long equipoId) {
        try {
            String url = equiposServiceUrl + "/equipos/" + equipoId;
            log.info("Llamando a microservicio de Equipos: {}", url);

            EquipoSimpleDto equipo = restTemplate.getForObject(url, EquipoSimpleDto.class);

            if (equipo == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Equipo con ID " + equipoId + " no encontrado");
            }

            // Validar que tenga coordenadas
            if (equipo.getLatitud() == null || equipo.getLongitud() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El equipo con ID " + equipoId + " no tiene coordenadas GPS configuradas");
            }

            return equipo;

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al obtener equipo con ID {}: {}", equipoId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Error al comunicarse con el servicio de Equipos: " + e.getMessage());
        }
    }
}