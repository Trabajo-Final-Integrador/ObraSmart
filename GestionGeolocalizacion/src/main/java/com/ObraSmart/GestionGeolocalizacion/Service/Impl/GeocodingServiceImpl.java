package com.ObraSmart.GestionGeolocalizacion.Service.Impl;

import com.ObraSmart.GestionGeolocalizacion.Service.IGeocodingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class GeocodingServiceImpl implements IGeocodingService {

    private static final Logger log = LoggerFactory.getLogger(GeocodingServiceImpl.class);

    private final RestTemplate restTemplate;

    private static final String NOMINATIM_URL =
            "https://nominatim.openstreetmap.org/search?q=%s&format=json&addressdetails=1&limit=1";

    public GeocodingServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public double[] obtenerCoordenadas(String direccion) {
        try {
            String encoded = URLEncoder.encode(direccion, StandardCharsets.UTF_8);
            String url = String.format(NOMINATIM_URL, encoded);

            // ✅ Headers obligatorios
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.USER_AGENT, "ObraSmart/1.0 (contacto@obrasmart.com)");
            headers.add(HttpHeaders.REFERER, "https://obrasmart.com");
            headers.add(HttpHeaders.ACCEPT_LANGUAGE, "es-AR");
            headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            log.info("🌎 Consultando Nominatim: {}", url);

            ResponseEntity<List> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    List.class
            );

            List<?> body = response.getBody();

            if (body == null || body.isEmpty()) {
                throw new RuntimeException("No se encontraron coordenadas para la dirección proporcionada.");
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> first = (Map<String, Object>) body.get(0);
            double lat = Double.parseDouble(first.get("lat").toString());
            double lon = Double.parseDouble(first.get("lon").toString());

            log.info("✅ Coordenadas obtenidas para '{}': lat={}, lon={}", direccion, lat, lon);
            return new double[]{lat, lon};

        } catch (HttpClientErrorException.Forbidden e) {
            log.error("🚫 Acceso prohibido (403) por Nominatim. Verificar User-Agent o límite de peticiones.");
            throw new RuntimeException("Nominatim rechazó la solicitud (403 Forbidden).");
        } catch (HttpClientErrorException e) {
            log.error("❌ Error HTTP al consultar Nominatim: {}", e.getMessage());
            throw new RuntimeException("Error HTTP al comunicarse con el servicio de geolocalización.");
        } catch (Exception e) {
            log.error("❌ Error general: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo obtener la geolocalización: " + e.getMessage());
        }
    }
}
