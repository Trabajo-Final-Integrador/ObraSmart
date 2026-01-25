package com.ObraSmart.GestionLogistica.dto;

import java.util.List;

public record AuthIdentityDto(
        Long userId,
        List<String> roles
) {}
