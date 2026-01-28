package com.obrasmart.identity.service;

import com.obrasmart.identity.config.ResetPasswordProperties;
import com.obrasmart.identity.dto.ResetRequest;
import com.obrasmart.identity.entity.PasswordResetToken;
import com.obrasmart.identity.entity.User;
import com.obrasmart.identity.repository.PasswordResetTokenRepository;
import com.obrasmart.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final ResetPasswordProperties resetProps;

    @Transactional
    public void requestReset(String email) {
        log.info("Password reset solicitado para {}", email);
        User user = userRepository.findByEmailIgnoreCase(email).orElse(null);
        if (user == null) {
            log.warn("Password reset: email no encontrado {}", email);
            return;
        }

        tokenRepository.deleteByUser_Id(user.getId());

        String token = UUID.randomUUID().toString().replace("-", "");
        Instant expiresAt = Instant.now().plusSeconds(resetProps.getTokenTtlMin() * 60L);

        PasswordResetToken prt = PasswordResetToken.builder()
                .user(user)
                .token(token)
                .expiresAt(expiresAt)
                .build();
        tokenRepository.save(prt);

        String link = buildResetLink(email, token);
        sendResetEmail(user.getEmail(), link);
    }

    @Transactional
    public void reset(ResetRequest request) {
        if (request.getToken() == null || request.getToken().isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "Token requerido");
        }

        PasswordResetToken token = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Token inválido"));

        if (token.getUsedAt() != null) {
            throw new ResponseStatusException(BAD_REQUEST, "Token ya utilizado");
        }
        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw new ResponseStatusException(BAD_REQUEST, "Token expirado");
        }

        User user = token.getUser();
        if (!user.getEmail().equalsIgnoreCase(request.getEmail())) {
            throw new ResponseStatusException(BAD_REQUEST, "Email no coincide");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        token.setUsedAt(Instant.now());
    }

    private String buildResetLink(String email, String token) {
        String base = resetProps.getFrontendBaseUrl();
        String encEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);
        String encToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
        return base + "/auth/reset-password?email=" + encEmail + "&token=" + encToken;
    }

    private void sendResetEmail(String to, String link) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            String fromName = resetProps.getFromName();
            String fromEmail = resetProps.getFromEmail();
            if (fromName != null && !fromName.isBlank()) {
                msg.setFrom(String.format("%s <%s>", fromName, fromEmail));
            } else {
                msg.setFrom(fromEmail);
            }
            msg.setSubject("Recuperación de contraseña - ObraSmart");
            msg.setText("Solicitaste restablecer tu contraseña.\n\n" +
                    "Usá este enlace para continuar:\n" + link + "\n\n" +
                    "Si no fuiste vos, ignorá este mensaje.");
            mailSender.send(msg);
            log.info("Password reset email enviado a {}", to);
        } catch (Exception ex) {
            log.error("Error enviando email de reset a {}: {}", to, ex.getMessage(), ex);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "No se pudo enviar el correo");
        }
    }
}
