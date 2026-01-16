package com.obrasmart.gestionauth.config;

import com.obrasmart.gestionauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@RequiredArgsConstructor
public class UserDetailsServiceConfig {

    private final UserRepository userRepository;

    /**
     * Carga usuarios para Spring Security.
     * Permite iniciar sesión con username O email.
     * Asigna authorities con el prefijo ROLE_ requerido por Spring.
     * Es la pieza que usa el AuthenticationManager para validar credenciales.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return usernameOrEmail -> userRepository.findByUsernameIgnoreCase(usernameOrEmail)
                .or(() -> userRepository.findByEmailIgnoreCase(usernameOrEmail))
                .map(u -> User.withUsername(u.getUsername())
                        .password(u.getPasswordHash())
                        .roles(u.getRole().name()) // agrega prefijo ROLE_
                        .disabled(u.getStatus() != com.obrasmart.gestionauth.entity.Status.ACTIVO)
                        .accountLocked(false)
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + usernameOrEmail));
    }
}
