package com.ObraSmart.GestionGeolocalizacion.Service.Impl;

import com.ObraSmart.GestionGeolocalizacion.Dto.ReparacionLiteDTO;
import com.ObraSmart.GestionGeolocalizacion.Service.IReparacionesClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ReparacionesClientServiceImpl implements IReparacionesClientService {

    private final RestTemplate restTemplate;

    @Value("${microservicio.reparaciones.url}")
    private String reparacionesUrl;

    public ReparacionesClientServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<ReparacionLiteDTO> obtenerTodas() {
        ReparacionLiteDTO[] arr = restTemplate.getForObject(reparacionesUrl, ReparacionLiteDTO[].class);
        return arr == null ? List.of() : Arrays.asList(arr);
    }
}
