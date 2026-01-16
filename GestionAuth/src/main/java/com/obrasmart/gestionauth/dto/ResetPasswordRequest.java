package com.obrasmart.gestionauth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Solicitud para aplicar un reseteo de contraseña con token previamente emitido.
 */
public record ResetPasswordRequest(
        @NotBlank String token,
        @NotBlank @Size(min = 6) String newPassword
) {}
