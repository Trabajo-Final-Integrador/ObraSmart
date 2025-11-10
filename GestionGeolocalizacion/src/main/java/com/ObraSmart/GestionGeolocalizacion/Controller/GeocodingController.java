package com.ObraSmart.GestionGeolocalizacion.Controller;

import com.ObraSmart.GestionGeolocalizacion.Service.IGeocodingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/geocoding")
public class GeocodingController {

    private final IGeocodingService geocodingService;

    public GeocodingController(IGeocodingService geocodingService) {
        this.geocodingService = geocodingService;
    }

    @GetMapping
    public ResponseEntity<double[]> getCoordenadas(@RequestParam String direccion) {
        double[] coordenadas = geocodingService.obtenerCoordenadas(direccion);
        return ResponseEntity.ok(coordenadas);
    }
}
