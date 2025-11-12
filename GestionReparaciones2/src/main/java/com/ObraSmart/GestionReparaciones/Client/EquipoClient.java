package com.ObraSmart.GestionReparaciones.Client;



import com.ObraSmart.GestionReparaciones.Exception.ExternalServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Cliente simple que consulta gestion-equipos por ID usando RestTemplate.
 * Lanza ExternalServiceException si hay error.
 */
@Component
public class EquipoClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public EquipoClient(RestTemplate restTemplate, @Value("${clients.gestion-equipos.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public EquipoDto getEquipoById(Long id) {
        try {
            String url = String.format("%s/equipos/%d", baseUrl, id);
            return restTemplate.getForObject(url, EquipoDto.class);
        } catch (RestClientException e) {
            throw new ExternalServiceException("Error consultando gestion-equipos: " + e.getMessage(), e);
        }
    }

    // DTO para parsear la respuesta (ajustá campos según tu servicio gestion-equipos)
    public static class EquipoDto {
        private Long id;
        private String nombre;
        private String estado;
        // getters y setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
    }
}
