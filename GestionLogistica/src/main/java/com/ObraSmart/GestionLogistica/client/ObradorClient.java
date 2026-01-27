package com.ObraSmart.GestionLogistica.client;


import com.ObraSmart.GestionLogistica.dto.ObradorSimpleDto;
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
public class ObradorClient {

    private final RestTemplate restTemplate;

    @Value("${microservices.obradores.url:http://localhost:8082}")
    private String obradoresServiceUrl;

    /**
     * Obtiene la información de un obrador por su ID
     * @param obradorId ID del obrador
     * @return Información del obrador con coordenadas
     */
    public ObradorSimpleDto obtenerObrador(Long obradorId) {
        try {
            String url = obradoresServiceUrl + "/obradores/" + obradorId;
            log.info("Llamando a microservicio de Obradores: {}", url);

            ObradorSimpleDto obrador = restTemplate.getForObject(url, ObradorSimpleDto.class);

            if (obrador == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Obrador con ID " + obradorId + " no encontrado");
            }

            // Validar que tenga coordenadas
            if (obrador.getLatitud() == null || obrador.getLongitud() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El obrador con ID " + obradorId + " no tiene coordenadas GPS configuradas");
            }

            return obrador;

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al obtener obrador con ID {}: {}", obradorId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Error al comunicarse con el servicio de Obradores: " + e.getMessage());
        }
    }
}