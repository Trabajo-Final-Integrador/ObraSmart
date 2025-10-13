package com.ObraSmart.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Collections;

/**
 * Filtro que valida el JWT recibido en Authorization: Bearer <token>
 * - No crea dependencias complejas para evitar ciclos con SecurityConfig.
 * - Solo valida firma y expiráción y carga el username en el SecurityContext.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String secretKey; // inyectado desde application.properties

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // No cargamos roles aquí (podés extraer claims.get("roles") si los generas)
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            // token expirado -> no autenticado
            logger.debug("Token expirado: " + ex.getMessage());
        } catch (Exception ex) {
            // token inválido -> no autenticado
            logger.debug("Token inválido: " + ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
