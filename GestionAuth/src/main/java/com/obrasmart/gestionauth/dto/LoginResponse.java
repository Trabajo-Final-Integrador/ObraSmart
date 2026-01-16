package com.obrasmart.gestionauth.dto;

/**
 * Respuesta estándar al autenticarse o consultar el usuario actual.
 * Incluye datos mínimos que usa el frontend para roles y estado.
 */
public record LoginResponse(
        Long id,
        String username,
        String email,
        String role,
        String status
) {}
