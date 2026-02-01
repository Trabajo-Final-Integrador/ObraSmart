package com.ObraSmart.GestionGeolocalizacion.Controller;

import com.ObraSmart.GestionGeolocalizacion.Dto.GeoResponseDTO;
import com.ObraSmart.GestionGeolocalizacion.Dto.ReverseGeoDTO;
import com.ObraSmart.GestionGeolocalizacion.Service.Impl.GeoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/geo")
@RequiredArgsConstructor
public class GeoController {

    private final GeoService geoService;

    @GetMapping("/geocode")
    public GeoResponseDTO geocode(@RequestParam String direccion) {
        return geoService.geocode(direccion);
    }

    @GetMapping("/reverse")
    public ReverseGeoDTO reverse(
            @RequestParam Double lat,
            @RequestParam Double lon) {

        return geoService.reverseGeocode(lat, lon);
    }

}
