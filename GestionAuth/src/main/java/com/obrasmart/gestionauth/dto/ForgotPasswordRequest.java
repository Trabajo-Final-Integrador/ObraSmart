package com.obrasmart.gestionauth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Solicitud para generar token de reseteo. En modo demo se informa por logs.
 */
public record ForgotPasswordRequest(
        @NotBlank @Email String email
) {}
