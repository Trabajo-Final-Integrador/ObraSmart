package com.ObraSmart.GestionReparaciones.Service;


import com.ObraSmart.GestionReparaciones.Exception.ExternalServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GeocodingServiceTest {

    private RestTemplate restTemplate;
    private GeocodingService geocodingService;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        geocodingService = new GeocodingService(restTemplate);

        // forza locationIQ vacío (usa Nominatim)
        TestUtils.setField(geocodingService, "locationIqKey", "");
    }

    @Test
    void testGeocodeAddress_ok() {

        // respuesta simulada de la API
        Map<String, Object> mockResponse = Map.of(
                "lat", "10.12345",
                "lon", "-20.54321"
        );
        Map[] body = new Map[]{ mockResponse };

        ResponseEntity<Map[]> entity =
                new ResponseEntity<>(body, HttpStatus.OK);

        // cuando hace exchange → devuelve el mock
        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                eq(HttpMethod.GET),
                ArgumentMatchers.<HttpEntity<?>>any(),
                ArgumentMatchers.<Class<Map[]>>any()
        )).thenReturn(entity);

        double[] result = geocodingService.geocodeAddress("Av. Siempre Viva 123");

        assertNotNull(result);
        assertEquals(10.12345, result[0]);
        assertEquals(-20.54321, result[1]);
    }

    @Test
    void testGeocodeAddress_null() {
        assertNull(geocodingService.geocodeAddress(null));
        assertNull(geocodingService.geocodeAddress(""));
    }

    @Test
    void testGeocodeAddress_restClientException() {

        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                ArgumentMatchers.<Class<Map[]>>any()
        )).thenThrow(new RuntimeException("Falla X"));

        assertThrows(ExternalServiceException.class,
                () -> geocodingService.geocodeAddress("Direccion X"));
    }
}
