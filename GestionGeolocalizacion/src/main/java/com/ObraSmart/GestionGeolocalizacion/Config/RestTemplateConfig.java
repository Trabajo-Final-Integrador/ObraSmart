package com.ObraSmart.GestionGeolocalizacion.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración global de RestTemplate para inyectar en servicios que consumen otros microservicios.
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("User-Agent", "ObraSmart-Geolocalizacion");
            return execution.execute(request, body);
        });return restTemplate;
    }
}
