package com.ObraSmart.GestionInspeccion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


import static org.springframework.aot.generate.ValueCodeGenerator.withDefaults;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    // 🔹 Define usuarios en memoria (para pruebas o entornos simples)
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("1234"))
                .roles("ADMINISTRADOR") // 👈 coincide con hasRole('ADMINISTRADOR')
                .build();

        UserDetails empleado = User.withUsername("empleado")
                .password(encoder.encode("abcd"))
                .roles("EMPLEADO")
                .build();

        return new InMemoryUserDetailsManager(admin, empleado);
    }

    // 🔹 Codificador de contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        // WARNING: NoOpPasswordEncoder stores/compares plain text. ONLY FOR DEV.
        return NoOpPasswordEncoder.getInstance();
    }

    // 🔹 Configuración del filtro de seguridad
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/empleados/**").hasRole("ADMINISTRADOR") // solo admin
                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> basic.disable());// Autenticación básica
        return http.build();
    }
}