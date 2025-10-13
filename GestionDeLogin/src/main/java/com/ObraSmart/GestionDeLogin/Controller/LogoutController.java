/*package com.ObraSmart.GestionDeLogin.Controller;

import com.ObraSmart.GestionDeLogin.Service.TokenBlacklistService;
import com.ObraSmart.GestionDeLogin.Config.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class LogoutController {

    private final TokenBlacklistService blacklistService;
    private final JwtService jwtService;

    public LogoutController(TokenBlacklistService blacklistService, JwtService jwtService) {
        this.blacklistService = blacklistService;
        this.jwtService = jwtService;
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        String token = jwtService.extractTokenFromHeader(authHeader);
        if (token == null) {
            return ResponseEntity.badRequest().body("Token no proporcionado");
        }

        blacklistService.blacklistToken(token);
        return ResponseEntity.ok("Sesión cerrada correctamente");
    }
}*/
package com.ObraSmart.GestionDeLogin.Controller;

import com.ObraSmart.GestionDeLogin.Config.JwtService;
import com.ObraSmart.GestionDeLogin.Service.TokenBlacklistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LogoutController {

    private final TokenBlacklistService blacklistService;
    private final JwtService jwtService;

    public LogoutController(TokenBlacklistService blacklistService, JwtService jwtService) {
        this.blacklistService = blacklistService;
        this.jwtService = jwtService;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("No token provided");
        }
        String token = authHeader.substring(7);
        blacklistService.blacklist(token);
        return ResponseEntity.ok().body("Logged out");
    }
}
