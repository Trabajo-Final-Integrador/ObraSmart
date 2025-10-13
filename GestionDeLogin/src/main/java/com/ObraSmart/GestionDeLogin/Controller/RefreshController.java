package com.ObraSmart.GestionDeLogin.Controller;

import com.ObraSmart.GestionDeLogin.Config.JwtService;
import com.ObraSmart.GestionDeLogin.Entity.User;
import com.ObraSmart.GestionDeLogin.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class RefreshController {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public RefreshController(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        if (refreshToken == null) return ResponseEntity.badRequest().body("refreshToken required");

        // validar que sea refresh token
        if (!jwtService.isRefreshToken(refreshToken)) {
            return ResponseEntity.status(401).body("Invalid refresh token");
        }
        if (jwtService.isTokenExpired(refreshToken)) {
            return ResponseEntity.status(401).body("Refresh token expired");
        }

        String username = jwtService.extractSubject(refreshToken);
        Optional<User> u = userRepository.findByUsername(username);
        if (u.isEmpty()) return ResponseEntity.status(404).body("User not found");

        String newAccess = jwtService.generateAccessToken(u.get());
        String newRefresh = jwtService.generateRefreshToken(u.get()); // opcional rotación

        return ResponseEntity.ok(Map.of("accessToken", newAccess, "refreshToken", newRefresh));
    }
}
