package com.ObraSmart.GestionGeolocalizacion.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Provee un RestTemplate simple para consumir otros microservicios o APIs externas.
 * (Se utiliza en los servicios de cliente y geocoding).
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
