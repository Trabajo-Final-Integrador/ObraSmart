package com.obrasmart.mediaservice.dto;

import java.util.List;

public record ByOwnersRequest(String ownerType, String purpose, List<String> ownerIds) {}
