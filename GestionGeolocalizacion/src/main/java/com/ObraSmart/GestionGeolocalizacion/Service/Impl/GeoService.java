package com.ObraSmart.GestionGeolocalizacion.Service.Impl;

import com.ObraSmart.GestionGeolocalizacion.Dto.GeoResponseDTO;
import com.ObraSmart.GestionGeolocalizacion.Dto.ReverseGeoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeoService {

    private final RestTemplate restTemplate;

    public GeoResponseDTO geocode(String direccion) {

        String url = "https://nominatim.openstreetmap.org/search?q="
                + direccion.replace(" ", "+")
                + "&format=json&limit=1";

        ResponseEntity<List<Map<String, Object>>> response =
                restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<>() {});

        if (response.getBody() == null || response.getBody().isEmpty()) {
            throw new RuntimeException("No se encontraron coordenadas");
        }

        Map<String, Object> result = response.getBody().get(0);

        GeoResponseDTO dto = new GeoResponseDTO();
        dto.setLat(Double.valueOf(result.get("lat").toString()));
        dto.setLon(Double.valueOf(result.get("lon").toString()));

        return dto;
    }

    public ReverseGeoDTO reverseGeocode(Double lat, Double lon) {

        String url = "https://nominatim.openstreetmap.org/reverse?lat="
                + lat + "&lon=" + lon + "&format=json";

        ResponseEntity<Map<String, Object>> response =
                restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<>() {});

        Map<String, Object> body = response.getBody();

        if (body == null || !body.containsKey("display_name")) {
            throw new RuntimeException("No se encontró dirección");
        }

        ReverseGeoDTO dto = new ReverseGeoDTO();
        dto.setDireccion(body.get("display_name").toString());

        return dto;
    }

}
