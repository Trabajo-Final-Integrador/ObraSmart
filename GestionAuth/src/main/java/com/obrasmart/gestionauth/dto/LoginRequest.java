package com.obrasmart.gestionauth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Payload de autenticación. Se admite username o email como identificador.
 */
public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password
) {}
