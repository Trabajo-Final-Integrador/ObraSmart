package com.obrasmart.mediaservice.dto;

import java.time.Instant;

public record EquipmentImageResponse(
    String equipoId,
    String sourceType,
    String imageUrl,
    String fileName,
    String contentType,
    Long size,
    Instant updatedAt
) {}
