package com.obrasmart.gestionauth.service;

import com.obrasmart.gestionauth.entity.PasswordResetToken;
import com.obrasmart.gestionauth.entity.User;
import com.obrasmart.gestionauth.repository.PasswordResetTokenRepository;
import com.obrasmart.gestionauth.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Crea un token de reseteo para el email indicado.
     * Flujo académico: no envía mails; se deja el token en logs para pruebas.
     * Se asocia a un usuario y caduca en 30 minutos.
     */
    public void createResetToken(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuario no encontrado"));

        String token = UUID.randomUUID().toString();
        PasswordResetToken prt = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(Instant.now().plus(30, ChronoUnit.MINUTES))
                .used(false)
                .build();
        tokenRepository.save(prt);

        // Modo demo: logueamos el token en server logs.
        log.info("Password reset token for {}: {}", email, token);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken prt = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Token inválido"));

        if (prt.isUsed()) {
            throw new ResponseStatusException(BAD_REQUEST, "Token ya utilizado");
        }

        if (prt.getExpiryDate().isBefore(Instant.now())) {
            throw new ResponseStatusException(BAD_REQUEST, "Token expirado");
        }

        // Si el token es válido, se reemplaza el hash BCrypt de la contraseña
        User user = prt.getUser();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        prt.setUsed(true);

        userRepository.save(user);
        tokenRepository.save(prt);
    }
}
