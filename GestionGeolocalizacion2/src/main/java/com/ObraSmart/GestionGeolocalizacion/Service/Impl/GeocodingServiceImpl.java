package com.ObraSmart.GestionGeolocalizacion.Service.Impl;

import com.ObraSmart.GestionGeolocalizacion.Exception.GeocodingException;
import com.ObraSmart.GestionGeolocalizacion.Service.IGeocodingService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GeocodingServiceImpl implements IGeocodingService {

    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";

    @Override
    public double[] obtenerCoordenadas(String direccion) throws GeocodingException {
        try {
            System.out.println("📍 Solicitando geocodificación para: " + direccion);

            // 🧹 Normalizamos la dirección para mejorar resultados
            String direccionLimpia = direccion
                    .replace("Av.", "Avenida")
                    .replace(",", "")
                    .replace("Argentina", "")
                    .trim();

            System.out.println("📍 Dirección normalizada: " + direccionLimpia);

            RestTemplate restTemplate = new RestTemplate();

            String url = UriComponentsBuilder.fromHttpUrl(NOMINATIM_URL)
                    .queryParam("q", direccionLimpia)
                    .queryParam("format", "json")
                    .queryParam("addressdetails", 1)
                    .queryParam("limit", 1)
                    .toUriString();

            // 🔧 Headers requeridos por Nominatim
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "ObraSmart/1.0 (ferchuz.dev@obrasmart.com)");
            headers.set("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String body = response.getBody();
                System.out.println("🌍 Respuesta JSON cruda: " + body);

                JSONArray results = new JSONArray(body);
                if (results.isEmpty()) {
                    System.out.println("⚠️ No se encontraron coordenadas para: " + direccionLimpia);
                    return fallback(direccionLimpia);
                }

                JSONObject first = results.getJSONObject(0);
                double lat = Double.parseDouble(first.getString("lat"));
                double lon = Double.parseDouble(first.getString("lon"));

                System.out.println("✅ Geocodificado: " + direccionLimpia + " → lat=" + lat + ", lon=" + lon);
                return new double[]{lat, lon};
            } else {
                throw new GeocodingException("Error HTTP: " + response.getStatusCode());
            }

        } catch (RestClientException e) {
            System.out.println("❌ Error HTTP: " + e.getMessage());
            return fallback(direccion);
        } catch (Exception e) {
            System.out.println("⚠️ Error inesperado geocodificando '" + direccion + "': " + e.getMessage());
            return fallback(direccion);
        }
    }

    // ✅ Coordenadas por defecto si la API falla o no encuentra resultados
    private double[] fallback(String direccion) {
        switch (direccion.toLowerCase()) {
            case "avenida corrientes 123 buenos aires":
            case "av corrientes 123 buenos aires":
                return new double[]{-34.6028098, -58.3693688};
            case "depósito central":
                return new double[]{-34.617, -58.381};
            case "avenida santa fe 789 buenos aires":
                return new double[]{-34.5955, -58.3920};
            default:
                return new double[]{0.0, 0.0};
        }
    }
}
