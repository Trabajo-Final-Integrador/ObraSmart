package com.ObraSmart.GestionReparaciones.service;

import com.ObraSmart.GestionReparaciones.exception.ExternalServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class GeocodingService {

    @Value("${locationiq.key:}")
    private String locationIqKey;

    private final RestTemplate rest;

    public GeocodingService(RestTemplate rest) {
        this.rest = rest;
    }

    public double[] geocodeAddress(String direccion) {
        if (direccion == null || direccion.isBlank()) return null;

        try {

            // para evitar problemas de caracteres especiales
            String encoded = URLEncoder.encode(direccion, StandardCharsets.UTF_8);

            String url;
            if (locationIqKey != null && !locationIqKey.isBlank()) {
                url = UriComponentsBuilder
                        .fromUriString("https://us1.locationiq.com/v1/search.php")
                        .queryParam("key", locationIqKey)
                        .queryParam("q", encoded)
                        .queryParam("format", "json")
                        .queryParam("limit", 1)
                        .toUriString();
            } else {
                url = UriComponentsBuilder
                        .fromUriString("https://nominatim.openstreetmap.org/search")
                        .queryParam("q", encoded)
                        .queryParam("format", "json")
                        .queryParam("limit", 1)
                        .toUriString();
            }

            // Nominatim NECESITA User-Agent
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "ObraSmart-Reparaciones/1.0");

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Map[]> response = rest.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map[].class
            );

            Map[] resp = response.getBody();

            if (resp != null && resp.length > 0) {
                Map first = resp[0];

                double lat = Double.parseDouble(first.get("lat").toString());
                double lon = Double.parseDouble(first.get("lon").toString());

                return new double[]{lat, lon};
            }

        } catch (RestClientException e) {
            throw new ExternalServiceException("Error en servicio de geocoding: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new ExternalServiceException("Procesamiento inválido de geocoding: " + e.getMessage(), e);
        }

        return null;
    }
}
