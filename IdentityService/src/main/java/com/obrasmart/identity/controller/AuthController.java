package com.obrasmart.identity.controller;

import com.obrasmart.identity.dto.ForgotRequest;
import com.obrasmart.identity.dto.LoginRequest;
import com.obrasmart.identity.dto.ResetRequest;
import com.obrasmart.identity.dto.SessionUser;
import com.obrasmart.identity.service.AuthService;
import com.obrasmart.identity.service.PasswordResetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {
        authService.login(request, response);
        return ResponseEntity.ok(Map.of("message", "login ok"));
    }

    @GetMapping("/me")
    public ResponseEntity<SessionUser> me(Authentication auth) {
        Long userId = auth != null ? (Long) auth.getPrincipal() : null;
        return ResponseEntity.ok(authService.me(userId));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forgot")
    public ResponseEntity<?> forgot(@RequestBody @Valid ForgotRequest request) {
        passwordResetService.requestReset(request.getEmail());
        return ResponseEntity.ok(Map.of("message", "Si el correo existe, recibirás instrucciones"));
    }

    @PostMapping("/reset")
    public ResponseEntity<?> reset(@RequestBody @Valid ResetRequest request) {
        passwordResetService.reset(request);
        return ResponseEntity.ok(Map.of("message", "Password actualizado"));
    }
}
