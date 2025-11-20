package com.ObraSmart.GestionAsistente.Config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.util.Locale;

@Configuration
public class LocaleConfig {

    @Bean
    public LocaleResolver localeResolver() {
        // Configura locale fijo para todo el proyecto
        return new FixedLocaleResolver(new Locale("es", "AR"));
    }
}

