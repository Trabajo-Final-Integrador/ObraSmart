package com.obrasmart.gestionauth.service;

import com.obrasmart.gestionauth.dto.LoginResponse;
import com.obrasmart.gestionauth.entity.User;
import com.obrasmart.gestionauth.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Capa de servicio para autenticación y sesión.
 * - Usa AuthenticationManager (de Spring Security) para validar credenciales.
 * - Guarda la autenticación en el SecurityContextHolder.
 * - Obliga la creación/uso de la sesión HTTP (JSESSIONID) para siguientes llamadas.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    /**
     * Autentica credenciales (usuario o email + password).
     * Si es correcto, Spring Security almacena la autenticación en el contexto y
     * se utiliza la sesión HTTP (JSESSIONID) para mantener al usuario logueado.
     */
    public LoginResponse login(String usernameOrEmail, String password, HttpServletRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usernameOrEmail, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Garantiza que exista sesión (JSESSIONID)
        request.getSession(true);

        User u = findUser(authentication.getName());
        return toResponse(u);
    }

    /**
     * Devuelve el usuario autenticado actual leyendo el SecurityContextHolder (sesión activa).
     */
    public LoginResponse currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            throw new IllegalStateException("No autenticado");
        }

        User u = findUser(auth.getName());
        return toResponse(u);
    }

    /**
     * Cierra la sesión actual invalidando la cookie JSESSIONID.
     */
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
    }

    private LoginResponse toResponse(User u) {
        return new LoginResponse(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getRole().name(),
                u.getStatus().name()
        );
    }

    /**
     * Permite autenticarse tanto con username como con email.
     */
    private User findUser(String identifier) {
        return userRepository.findByUsernameIgnoreCase(identifier)
                .or(() -> userRepository.findByEmailIgnoreCase(identifier))
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
    }
}
