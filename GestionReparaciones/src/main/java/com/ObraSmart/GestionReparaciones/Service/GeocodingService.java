package com.ObraSmart.GestionReparaciones.Service;


import com.ObraSmart.GestionReparaciones.Exception.ExternalServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Map;

@Service
public class GeocodingService {

    @Value("${locationiq.key:}")
    private String locationIqKey;

    private final RestTemplate rest;

    public GeocodingService(RestTemplate rest) {
        this.rest = rest;
    }

    /**
     * Devuelve {lat, lon} o null si no pudo geocodificar.
     * Lanza ExternalServiceException si falla la comunicación.
     */
    public double[] geocodeAddress(String direccion) {
        if (direccion == null || direccion.isBlank()) return null;
        try {
            if (locationIqKey != null && !locationIqKey.isBlank()) {
                String url = UriComponentsBuilder.fromUriString("https://us1.locationiq.com/v1/search.php")
                        .queryParam("key", locationIqKey)
                        .queryParam("q", direccion)
                        .queryParam("format", "json")
                        .queryParam("limit", 1)
                        .toUriString();
                Map[] resp = rest.getForObject(url, Map[].class);
                if (resp != null && resp.length > 0) {
                    Map first = resp[0];
                    double lat = Double.parseDouble(first.get("lat").toString());
                    double lon = Double.parseDouble(first.get("lon").toString());
                    return new double[]{lat, lon};
                }
            } else {
                String url = UriComponentsBuilder.fromUriString("https://nominatim.openstreetmap.org/search")
                        .queryParam("q", direccion)
                        .queryParam("format", "json")
                        .queryParam("limit", 1)
                        .toUriString();
                Map[] resp = rest.getForObject(url, Map[].class);
                if (resp != null && resp.length > 0) {
                    Map first = resp[0];
                    double lat = Double.parseDouble(first.get("lat").toString());
                    double lon = Double.parseDouble(first.get("lon").toString());
                    return new double[]{lat, lon};
                }
            }
        } catch (RestClientException e) {
            throw new ExternalServiceException("Error en servicio de geocoding: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new ExternalServiceException("Procesamiento inválido de geocoding: " + e.getMessage(), e);
        }
        return null;
    }
}

