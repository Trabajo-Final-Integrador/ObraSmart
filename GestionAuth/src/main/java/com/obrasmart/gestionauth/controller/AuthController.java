package com.obrasmart.gestionauth.controller;

import com.obrasmart.gestionauth.dto.ForgotPasswordRequest;
import com.obrasmart.gestionauth.dto.LoginRequest;
import com.obrasmart.gestionauth.dto.LoginResponse;
import com.obrasmart.gestionauth.dto.ResetPasswordRequest;
import com.obrasmart.gestionauth.service.AuthService;
import com.obrasmart.gestionauth.service.PasswordResetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Capa de entrada (API REST) para autenticación y recuperación de acceso.
 * Solo delega en los servicios; no contiene lógica de negocio.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    /**
     * Recibe credenciales, delega autenticación al servicio y deja la sesión activa (JSESSIONID).
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req, HttpServletRequest request) {
        return ResponseEntity.ok(authService.login(req.username(), req.password(), request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.ok().build();
    }

    // Preferido para front: devuelve el usuario autenticado actual
    @GetMapping("/me")
    public ResponseEntity<LoginResponse> me() {
        return ResponseEntity.ok(authService.currentUser());
    }

    // Alias retro-compatibilidad
    @GetMapping("/id")
    public ResponseEntity<LoginResponse> id() {
        return ResponseEntity.ok(authService.currentUser());
    }

    /**
     * Solicita token de reseteo de contraseña (modo demo: se loguea en servidor).
     */
    @PostMapping({"/forgot", "/forgot-password"})
    public ResponseEntity<Void> forgot(@Valid @RequestBody ForgotPasswordRequest req) {
        passwordResetService.createResetToken(req.email());
        return ResponseEntity.ok().build();
    }

    /**
     * Resetea la contraseña con el token emitido previamente.
     */
    @PostMapping({"/reset", "/reset-password"})
    public ResponseEntity<Void> reset(@Valid @RequestBody ResetPasswordRequest req) {
        passwordResetService.resetPassword(req.token(), req.newPassword());
        return ResponseEntity.ok().build();
    }
}
