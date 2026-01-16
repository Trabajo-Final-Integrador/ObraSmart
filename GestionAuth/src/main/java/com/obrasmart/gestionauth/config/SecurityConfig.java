package com.obrasmart.gestionauth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Configuración central de Spring Security en modo sesión (JSESSIONID).
     * - Sin JWT ni filtros custom.
     * - Sesiones "stateful" para que el gateway y el frontend trabajen con cookies.
     * - Rutas públicas: login, recuperación de clave y healthcheck.
     * - Resto de rutas protegidas.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/login",
                                "/auth/forgot", "/auth/forgot-password",
                                "/auth/reset", "/auth/reset-password",
                                "/actuator/health"
                        ).permitAll()
                        .requestMatchers("/auth/seed-admin").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(fl -> fl.disable())
                .httpBasic(hb -> hb.disable());

        return http.build();
    }

    /**
     * BCrypt se usa para almacenar y validar contraseñas de forma segura.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Exponemos AuthenticationManager para que AuthService pueda autenticar usuarios
     * con el UserDetailsService configurado por Spring.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
