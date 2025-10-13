/*package com.ObraSmart.GestionDeLogin.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class RecoverController {

    private final com.ObraSmart.GestionDeLogin.Config.JwtService jwtService;
    private final RestTemplate restTemplate = new RestTemplate();
    // URL de GestionUsuario
    private final String gestionUsuarioUrl = "http://localhost:8082";

    @PostMapping("/recover-password")
    public ResponseEntity<?> recoverPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message","El email es requerido"));
        }

        // Consultar a GestionUsuario si existe el usuario
        String checkUrl = gestionUsuarioUrl + "/users/exists?email=" + email;
        try {
            ResponseEntity<Boolean> resp = restTemplate.getForEntity(checkUrl, Boolean.class);
            Boolean exists = resp.getBody();
            if (exists == null || !exists) {
                // Por seguridad: no indicar si existe o no en producción; aquí devolvemos mensaje genérico
                return ResponseEntity.ok(Map.of("message","Si existe el usuario se ha enviado el email"));
            }
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Map.of("message","Error al verificar usuario: "+ex.getMessage()));
        }

        // Generar token de recuperación
        String token = jwtService.generateResetToken(email);

        // En producción: enviar email con link que contenga token.
        // Para pruebas devolvemos token en la respuesta:
        Map<String, String> result = new HashMap<>();
        result.put("message","Token generado");
        result.put("resetToken", token);
        return ResponseEntity.ok(result);
    }
}*/
/*package com.ObraSmart.GestionDeLogin.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class RecoverController {

    private final com.ObraSmart.GestionDeLogin.Config.JwtService jwtService;

    private final RestTemplate restTemplate = new RestTemplate();
    // URL de GestionUsuario
    private final String gestionUsuarioUrl = "http://localhost:8082";

    public RecoverController(com.ObraSmart.GestionDeLogin.Config.JwtService jwtService) {
        this.jwtService = jwtService;
    }
    // ===================== RECUPERAR CONTRASEÑA =====================
    @PostMapping("/recover-password")
    public ResponseEntity<?> recoverPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message","El email es requerido"));
        }

        // Consultar a GestionUsuario si existe el usuario
        String checkUrl = gestionUsuarioUrl + "/users/exists?email=" + email;
        try {
            ResponseEntity<Boolean> resp = restTemplate.getForEntity(checkUrl, Boolean.class);
            Boolean exists = resp.getBody();
            if (exists == null || !exists) {
                // Por seguridad: no indicar si existe o no en producción; aquí devolvemos mensaje genérico
                return ResponseEntity.ok(Map.of("message","Si existe el usuario se ha enviado el email"));
            }
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Map.of("message","Error al verificar usuario: "+ex.getMessage()));
        }

        // Generar token de recuperación
        String token = jwtService.generateResetToken(email);

        // En producción: enviar email con link que contenga token.
        // Para pruebas devolvemos token en la respuesta:
        Map<String, String> result = new HashMap<>();
        result.put("message","Token generado");
        result.put("resetToken", token);
        return ResponseEntity.ok(result);
    }

    // ===================== RESTABLECER CONTRASEÑA =====================
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String token = body.get("resetToken");
        String newPassword = body.get("newPassword");

        if (email == null || email.isBlank() ||
                token == null || token.isBlank() ||
                newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email, token y nueva contraseña son requeridos"));
        }

        // Validar el token
        if (!jwtService.isResetTokenValid(token, email)) {
            return ResponseEntity.status(401).body(Map.of("message", "Token inválido o expirado"));
        }

        // Llamar a GestionUsuario para actualizar la contraseña
        String resetUrl = gestionUsuarioUrl + "/users/reset-password";
        Map<String, String> request = new HashMap<>();
        request.put("email", email);
        request.put("newPassword", newPassword);

        try {
            restTemplate.put(resetUrl, request);
            return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente"));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Map.of("message", "Error al actualizar contraseña: " + ex.getMessage()));
        }
    }
}*/
package com.ObraSmart.GestionDeLogin.Controller;

import com.ObraSmart.GestionDeLogin.Config.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class RecoverController {

    private final JwtService jwtService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String gestionUsuarioUrl = "http://localhost:8082"; // ajustar si es distinto

    public RecoverController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/recover-password")
    public ResponseEntity<?> recover(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) return ResponseEntity.badRequest().body("email required");

        // check existence at GestionUsuario
        try {
            Boolean exists = restTemplate.getForObject(gestionUsuarioUrl + "/users/exists?email=" + email, Boolean.class);
            // No revelar si existe o no en produccion; aquí devolvemos token solo si existe
            if (exists == null || !exists) {
                // Return 200 generic message to avoid email enumeration
                return ResponseEntity.ok(Map.of("message","Si existe una cuenta, se envió un correo"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error contacting user service: " + e.getMessage());
        }

        // Generar reset token
        String resetToken = jwtService.generateResetToken(email);

        // En producción: enviar email con link. Para pruebas devolvemos token.
        return ResponseEntity.ok(Map.of("message","Token generado", "resetToken", resetToken));
    }
}
