package com.obrasmart.identity.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SessionUser {
    private Long userId;
    private List<String> roles;
    private String username;
    private String email;
    private String status;
}
