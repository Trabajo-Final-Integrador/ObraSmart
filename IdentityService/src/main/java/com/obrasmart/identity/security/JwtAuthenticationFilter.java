package com.obrasmart.identity.security;

import com.obrasmart.identity.config.CookieProperties;
import com.obrasmart.identity.config.JwtProperties;
import com.obrasmart.identity.entity.RefreshToken;
import com.obrasmart.identity.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final RefreshTokenService refreshTokenService;
    private final CookieProperties cookieProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = resolveAccessToken(request);

        if (accessToken != null && jwtService.isTokenValid(accessToken)) {
            setAuthentication(accessToken, request);
        } else {
            String refreshRaw = resolveRefreshToken(request);
            if (refreshRaw != null) {
                Optional<RefreshToken> valid = refreshTokenService.findValid(refreshRaw);
                if (valid.isPresent()) {
                    RefreshToken rt = valid.get();
                    User user = rt.getUser(); // ya viene fetch-joined

                    List<String> roles = List.of(user.getRole().name());
                    log.debug("Refresh válido para user={} roles={}", user.getUsername(), roles);

                    String newAccess = jwtService.generateAccessToken(
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getStatus().name(),
                            roles
                    );

                    setAuthCookies(response, newAccess, refreshRaw, request.isSecure());
                    setAuthentication(newAccess, request);
                } else {
                    log.debug("Refresh token inválido o expirado");
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String token, HttpServletRequest request) {
        Long userId = Long.parseLong(jwtService.extractClaim(token, c -> c.getSubject()));
        List<String> roles = jwtService.extractClaim(token, c -> c.get("roles", List.class));

        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .toList();

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userId, null, authorities);

        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private String resolveAccessToken(HttpServletRequest request) {
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        return Arrays.stream(cookies)
                .filter(c -> cookieProperties.getAccessName().equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    private String resolveRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        return Arrays.stream(cookies)
                .filter(c -> cookieProperties.getRefreshName().equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    private void setAuthCookies(HttpServletResponse response, String access, String refresh, boolean secureRequest) {
        ResponseCookie accessCookie = ResponseCookie.from(cookieProperties.getAccessName(), access)
                .httpOnly(true)
                .secure(secureRequest || cookieProperties.isSecure())
                .path("/")
                .sameSite(cookieProperties.getSameSite())
                .maxAge(jwtProperties.getAccessTtlMin() * 60L)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from(cookieProperties.getRefreshName(), refresh)
                .httpOnly(true)
                .secure(secureRequest || cookieProperties.isSecure())
                .path("/")
                .sameSite(cookieProperties.getSameSite())
                .maxAge(jwtProperties.getRefreshTtlH() * 3600L)
                .build();

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());
    }
}
