package com.ObraSmart.GestionReparaciones.Client;


import com.ObraSmart.GestionReparaciones.Exception.ExternalServiceException;
import com.ObraSmart.GestionReparaciones.Exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class EquipoClientImpl implements EquipoClient {

    private final RestTemplate restTemplate;

    // Ej: http://localhost:8087/equipos
    @Value("${equipos.service.url}")
    private String equiposServiceUrl;

    @Override
    public void validarEquipoExiste(Long equipoId) {
        String url = equiposServiceUrl + "/" + equipoId;
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
        // ⚠️ Esto depende de que en GestionEquipos agregues un endpoint.
        // Ejemplo: PATCH /equipos/{id}/estado?estado=EN_MANTENIMIENTO
        String url = equiposServiceUrl + "/" + equipoId + "/estado?estado=" + nuevoEstado;
        try {
            restTemplate.patchForObject(url, null, Void.class);
        } catch (RestClientException e) {
            // No rompemos la reparación, pero registramos error
            throw new ExternalServiceException(
                    "No se pudo actualizar el estado del equipo en el micro de Equipos",
                    HttpStatus.SERVICE_UNAVAILABLE
            );
        }
    }
}
