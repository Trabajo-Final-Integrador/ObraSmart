package com.obrasmart.identity.service;

import com.obrasmart.identity.config.CookieProperties;
import com.obrasmart.identity.config.JwtProperties;
import com.obrasmart.identity.dto.LoginRequest;
import com.obrasmart.identity.dto.SessionUser;
import com.obrasmart.identity.entity.User;
import com.obrasmart.identity.repository.UserRepository;
import com.obrasmart.identity.security.JwtService;
import com.obrasmart.identity.security.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import java.time.Instant;
import java.util.List;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties jwtProperties;
    private final CookieProperties cookieProperties;

    public void login(LoginRequest request, HttpServletResponse response) {

        User user = userRepository.findByUsernameIgnoreCase(request.getUsername())
                .or(() -> userRepository.findByEmailIgnoreCase(request.getUsername()))
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Credenciales inválidas"));

        if (!user.isEnabled() || !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(UNAUTHORIZED, "Credenciales inválidas");
        }

        List<String> roles = List.of(user.getRole().name());

        String access = jwtService.generateAccessToken(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getStatus().name(),
                roles
        );

        String refresh = jwtService.generateRefreshToken(user.getId(), roles);

        Instant refreshExp = Instant.now().plusSeconds(jwtProperties.getRefreshTtlH() * 3600L);
        refreshTokenService.create(user, refresh, refreshExp);

        setAuthCookies(response, access, refresh);
    }

    public SessionUser me(Long userId) {
        if (userId == null) throw new ResponseStatusException(UNAUTHORIZED);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED));

        return SessionUser.builder()
                .userId(user.getId())
                .roles(List.of(user.getRole().name()))
                .username(user.getUsername())
                .email(user.getEmail())
                .status(user.getStatus().name())
                .build();
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshRaw = resolveRefreshFromCookies(request);
        if (refreshRaw != null && !refreshRaw.isBlank()) {
            refreshTokenService.revoke(refreshRaw);
        }
        clearCookies(response);
    }

    private String resolveRefreshFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        return java.util.Arrays.stream(cookies)
                .filter(c -> cookieProperties.getRefreshName().equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    private void setAuthCookies(HttpServletResponse response, String access, String refresh) {
        boolean secure = cookieProperties.isSecure();

        ResponseCookie accessCookie = ResponseCookie.from(cookieProperties.getAccessName(), access)
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .sameSite(cookieProperties.getSameSite())
                .maxAge(jwtProperties.getAccessTtlMin() * 60L)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from(cookieProperties.getRefreshName(), refresh)
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .sameSite(cookieProperties.getSameSite())
                .maxAge(jwtProperties.getRefreshTtlH() * 3600L)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }

    private void clearCookies(HttpServletResponse response) {
        ResponseCookie accessCookie = ResponseCookie.from(cookieProperties.getAccessName(), "")
                .httpOnly(true)
                .secure(cookieProperties.isSecure())
                .path("/")
                .sameSite(cookieProperties.getSameSite())
                .maxAge(0)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from(cookieProperties.getRefreshName(), "")
                .httpOnly(true)
                .secure(cookieProperties.isSecure())
                .path("/")
                .sameSite(cookieProperties.getSameSite())
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }
}
