package com.ObraSmart.GestionGeolocalizacion.Service.Impl;

import com.ObraSmart.GestionGeolocalizacion.Dto.EquipoUbicacionDTO;
import com.ObraSmart.GestionGeolocalizacion.Service.IEquiposClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class EquiposClientServiceImpl implements IEquiposClientService {

    private final RestTemplate restTemplate;

    @Value("${microservicio.equipos.url}")
    private String equiposUrl;

    public EquiposClientServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<EquipoUbicacionDTO> obtenerTodos() {
        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    equiposUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );

            List<Map<String, Object>> rawEquipos = response.getBody();
            if (rawEquipos == null) {
                return List.of();
            }

            return rawEquipos.stream()
                    .map(this::mapearSeguro)
                    .filter(Objects::nonNull)
                    .toList();

        } catch (RestClientException ex) {
            System.out.println("Error consultando microservicio de equipos: " + ex.getMessage());
            return List.of();
        }
    }

    private EquipoUbicacionDTO mapearSeguro(Map<String, Object> raw) {
        if (raw == null) {
            return null;
        }

        try {
            EquipoUbicacionDTO dto = new EquipoUbicacionDTO();
            dto.setId(asLong(raw.get("id")));
            dto.setNombre(asString(raw.get("nombre")));
            dto.setUbicacionActual(asString(raw.get("ubicacionActual")));
            dto.setEstado(asString(raw.containsKey("estadoOperativo") ? raw.get("estadoOperativo") : raw.get("estado")));
            dto.setLatitud(asDouble(raw.get("latitud")));
            dto.setLongitud(asDouble(raw.get("longitud")));
            return dto;
        } catch (Exception ex) {
            System.out.println("Registro de equipo descartado por datos invalidos: " + ex.getMessage());
            return null;
        }
    }

    private String asString(Object value) {
        return value == null ? null : value.toString();
    }

    private Long asLong(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).longValue();
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private Double asDouble(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).doubleValue();
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}

