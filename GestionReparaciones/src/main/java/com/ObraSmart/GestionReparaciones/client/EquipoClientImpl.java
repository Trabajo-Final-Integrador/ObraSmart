package com.ObraSmart.GestionReparaciones.client;


import com.ObraSmart.GestionReparaciones.dto.EquipoDTO;
import com.ObraSmart.GestionReparaciones.exception.ExternalServiceException;
import com.ObraSmart.GestionReparaciones.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class EquipoClientImpl implements EquipoClient {

    private final RestTemplate restTemplate;

    @Value("${equipos.service.url}")
    private String equiposServiceUrl;

    @Override
    public void validarEquipoExiste(Long equipoId) {
        String url = equiposServiceUrl + "/equipos/" + equipoId;

        try {
            restTemplate.getForObject(url, Object.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Equipo no encontrado con id " + equipoId);
        } catch (RestClientException e) {
            throw new ExternalServiceException(
                    "No se pudo validar el equipo en el micro de Equipos",
                    HttpStatus.SERVICE_UNAVAILABLE
            );
        }
    }

    @Override
    public void actualizarEstadoEquipo(Long equipoId, String nuevoEstado) {

        String url = equiposServiceUrl + "/equipos/" + equipoId + "/estado?estado=" + nuevoEstado;

        try {
            HttpEntity<String> body = new HttpEntity<>("");
            restTemplate.exchange(url, HttpMethod.PATCH, body, Void.class);

        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Equipo no encontrado con id " + equipoId);
        } catch (RestClientException e) {
            throw new ExternalServiceException(
                    "No se pudo actualizar el estado del equipo en el micro de Equipos",
                    HttpStatus.SERVICE_UNAVAILABLE
            );
        }
    }

    public EquipoDTO obtenerEquipo(Long equipoId) {
        String url = equiposServiceUrl + "/equipos/" + equipoId;

        try {
            return restTemplate.getForObject(url, EquipoDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Equipo no encontrado con id " + equipoId);
        } catch (RestClientException e) {
            throw new ExternalServiceException(
                    "No se pudo consultar el micro de Equipos",
                    HttpStatus.SERVICE_UNAVAILABLE
            );
        }
    }


}
