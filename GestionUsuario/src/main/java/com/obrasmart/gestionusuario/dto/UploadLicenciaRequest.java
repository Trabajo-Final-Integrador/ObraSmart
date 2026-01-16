package com.obrasmart.gestionusuario.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO para cargar archivos de licencia (frente/dorso) usando multipart.
 */
public record UploadLicenciaRequest(
        @NotNull MultipartFile file
) {}
