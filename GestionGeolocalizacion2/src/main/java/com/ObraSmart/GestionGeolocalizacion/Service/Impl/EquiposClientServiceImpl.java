package com.ObraSmart.GestionGeolocalizacion.Service.Impl;

import com.ObraSmart.GestionGeolocalizacion.Dto.EquipoUbicacionDTO;
import com.ObraSmart.GestionGeolocalizacion.Service.IEquiposClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

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
        EquipoUbicacionDTO[] arr = restTemplate.getForObject(equiposUrl, EquipoUbicacionDTO[].class);
        return arr == null ? List.of() : Arrays.asList(arr);
    }
}

