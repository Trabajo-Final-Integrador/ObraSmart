package com.ObraSmart.Gateway.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

/**
 * Copia el ACCESS_TOKEN de la cookie al header Authorization si no viene presente.
 * Permite que microservicios que esperan Authorization Bearer lean el JWT sin cambiar el frontend.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthCookieToHeaderFilter extends OncePerRequestFilter {

    private static final String ACCESS_COOKIE = "ACCESS_TOKEN";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || authHeader.isBlank()) {
            String token = extractAccessToken(request.getCookies());
            if (token != null) {
                final String bearer = "Bearer " + token;
                HttpServletRequestWrapper wrapped = new HttpServletRequestWrapper(request) {
                    @Override
                    public String getHeader(String name) {
                        if (HttpHeaders.AUTHORIZATION.equalsIgnoreCase(name)) {
                            return bearer;
                        }
                        return super.getHeader(name);
                    }

                    @Override
                    public Enumeration<String> getHeaders(String name) {
                        if (HttpHeaders.AUTHORIZATION.equalsIgnoreCase(name)) {
                            return Collections.enumeration(List.of(bearer));
                        }
                        return super.getHeaders(name);
                    }

                    @Override
                    public Enumeration<String> getHeaderNames() {
                        List<String> names = Collections.list(super.getHeaderNames());
                        if (!names.contains(HttpHeaders.AUTHORIZATION)) {
                            names.add(HttpHeaders.AUTHORIZATION);
                        }
                        return Collections.enumeration(names);
                    }
                };
                filterChain.doFilter(wrapped, response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractAccessToken(Cookie[] cookies) {
        if (cookies == null) return null;
        return Arrays.stream(cookies)
                .filter(c -> ACCESS_COOKIE.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
