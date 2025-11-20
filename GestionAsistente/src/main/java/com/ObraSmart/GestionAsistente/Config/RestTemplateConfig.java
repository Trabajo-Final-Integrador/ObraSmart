package com.ObraSmart.GestionAsistente.Config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplateUtf8() {
        RestTemplate rest = new RestTemplate();

        // Reemplaza el converter por uno en UTF-8
        List converters = rest.getMessageConverters();
        converters.removeIf(c -> c instanceof StringHttpMessageConverter);
        converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));

        return rest;
    }
}
