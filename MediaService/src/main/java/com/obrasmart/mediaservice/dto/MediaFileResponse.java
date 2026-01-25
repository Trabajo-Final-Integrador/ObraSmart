package com.obrasmart.mediaservice.dto;

import java.time.Instant;
import java.util.UUID;

public record MediaFileResponse(
    UUID id,
    String ownerType,
    String ownerId,
    String purpose,
    String originalName,
    String contentType,
    long size,
    Instant createdAt
) {}
