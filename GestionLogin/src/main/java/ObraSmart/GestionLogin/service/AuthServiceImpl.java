package ObraSmart.GestionLogin.service;

import ObraSmart.GestionLogin.dto.ForgotPasswordRequest;

import ObraSmart.GestionLogin.dto.LoginRequestDto;
import ObraSmart.GestionLogin.dto.LoginResponse;
import ObraSmart.GestionLogin.dto.ResetPasswordRequest;
import ObraSmart.GestionLogin.entity.PasswordResetToken;
import ObraSmart.GestionLogin.repository.PasswordResetTokenRepository;
import ObraSmart.GestionLogin.repository.UserRepository;
import ObraSmart.GestionLogin.security.AppUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authManager;
    private final UserRepository users;
    private final PasswordResetTokenRepository tokens;
    private final PasswordEncoder encoder;

    @Override
    @Transactional
    public LoginResponse login(LoginRequestDto req, HttpServletRequest http) {
        var auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );

        // Establecer autenticación en el contexto
        SecurityContextHolder.getContext().setAuthentication(auth);

        // ✅ Guardar el contexto en la sesión manualmente
        var session = http.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());

        var principal = (AppUserDetails) auth.getPrincipal();
        var u = principal.getUser();
        u.setLastLoginAt(Instant.now());
        return new LoginResponse(u.getId(), u.getUsername(), u.getEmail(), u.getRole(), u.getStatus());
    }

    @Override
    public void logout(HttpServletRequest http) {
        var session = http.getSession(false);
        if (session != null) session.invalidate();
        SecurityContextHolder.clearContext();
    }

    @Override
    @Transactional
    public String forgot(ForgotPasswordRequest req) {
        var user = users.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email no registrado"));

        var token = PasswordResetToken.builder()
                .token(UUID.randomUUID().toString().replace("-", ""))
                .user(user)
                .expiresAt(Instant.now().plus(30, ChronoUnit.MINUTES))
                .used(false)
                .build();

        tokens.save(token);
        return token.getToken(); // lo devolvemos solo para pruebas
    }

    @Override
    @Transactional
    public void reset(ResetPasswordRequest req) {
        var t = tokens.findByToken(req.getToken())
                .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

        if (t.isUsed() || t.getExpiresAt().isBefore(Instant.now()))
            throw new IllegalArgumentException("Token expirado o usado");

        var u = t.getUser();
        u.setPassword(encoder.encode(req.getNewPassword()));
        t.setUsed(true);
    }
}