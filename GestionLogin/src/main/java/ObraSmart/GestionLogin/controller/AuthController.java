package ObraSmart.GestionLogin.controller;

import ObraSmart.GestionLogin.dto.ForgotPasswordRequest;
import ObraSmart.GestionLogin.dto.LoginRequestDto;
import ObraSmart.GestionLogin.dto.LoginResponse;
import ObraSmart.GestionLogin.dto.ResetPasswordRequest;
import ObraSmart.GestionLogin.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequestDto req, HttpServletRequest http) {
        return service.login(req, http);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest http) {
        service.logout(http);
    }

    @PostMapping("/forgot")
    public String forgot(@RequestBody ForgotPasswordRequest req) {
        return service.forgot(req);
    }

    @PostMapping("/reset")
    public void reset(@RequestBody ResetPasswordRequest req) {
        service.reset(req);
    }

    @GetMapping("/id")
    public Object getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("👤 Authenticated user: " + auth.getName());
        System.out.println("🔑 Authorities: " + auth.getAuthorities());
        return auth.getPrincipal();
    }
}