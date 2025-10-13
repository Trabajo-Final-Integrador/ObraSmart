package com.ObraSmart.GestionDeLogin.Config;

import com.ObraSmart.GestionDeLogin.Entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private String accessTokenStr;

    @Value("${jwt.refresh-token-ms}")
    private String refreshTokenStr;

    @Value("${jwt.reset-token-ms:15m}")
    private String resetTokenStr;

    private long accessTokenMs;
    private long refreshTokenMs;
    private long resetTokenMs;

    @PostConstruct
    public void init() {
        accessTokenMs = parseDuration(accessTokenStr);
        refreshTokenMs = parseDuration(refreshTokenStr);
        resetTokenMs = parseDuration(resetTokenStr);
    }

    private long parseDuration(String duration) {
        duration = duration.trim().toLowerCase();
        if (duration.endsWith("h")) {
            return Long.parseLong(duration.replace("h", "")) * 60 * 60 * 1000;
        } else if (duration.endsWith("m")) {
            return Long.parseLong(duration.replace("m", "")) * 60 * 1000;
        } else if (duration.endsWith("s")) {
            return Long.parseLong(duration.replace("s", "")) * 1000;
        }
        return Long.parseLong(duration); // valor en ms directo
    }

    private Key getSigningKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            return Keys.hmacShaKeyFor(secret.getBytes());
        }
    }

    // ==================== GENERACIÓN DE TOKENS ====================
    public String generateAccessToken(User user) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("id", user.getId())
                .claim("role", user.getRole() != null ? user.getRole().name() : null)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + accessTokenMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(User user) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("refresh", true)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + refreshTokenMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateResetToken(String emailOrUsername) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(emailOrUsername)
                .claim("reset", true)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + resetTokenMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ==================== EXTRACCIÓN ====================
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractAllClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    // ==================== VALIDACIONES ====================
    public boolean isAccessTokenValid(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRefreshToken(String token) {
        try {
            Boolean b = extractAllClaims(token).get("refresh", Boolean.class);
            return Boolean.TRUE.equals(b);
        } catch (Exception e) {
            return false;
        }
    }
    // ==================== EXTRACCIÓN ====================
    public String extractSubject(String token) {
        return extractUsername(token);
    }


    public boolean isResetToken(String token) {
        try {
            Boolean b = extractAllClaims(token).get("reset", Boolean.class);
            return Boolean.TRUE.equals(b);
        } catch (Exception e) {
            return false;
        }
    }
}
