package com.ObraSmart.GestionStock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            http
                    .csrf(csrf -> csrf.disable())            // desactiva CSRF
                    .authorizeHttpRequests(auth -> auth
                            .anyRequest().permitAll()            // permite todo
                    )
                    .formLogin(form -> form.disable())       // desactiva login
                    .httpBasic(basic -> basic.disable());    // desactiva basic auth
            return http.build();
        }
}