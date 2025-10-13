package com.ObraSmart.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 🚫 Desactivar CSRF para facilitar pruebas con Postman
                .csrf().disable()

                // ✅ Definir qué endpoints son públicos y cuáles requieren auth
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()              // Registro / Login sin autenticación
                        .requestMatchers(HttpMethod.GET, "/usuario/publico/**").permitAll() // Endpoints públicos
                        .anyRequest().authenticated()                         // Todo lo demás requiere login
                )

                // ⚡ No usar sesiones — importante si usás JWT o auth sin estado
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 🔐 Proveedor de autenticación
                .authenticationProvider(authProvider);

        // 📝 Si en el futuro activás JWT, acá se agrega el filtro
        // .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

        return http.build();
    }


}
