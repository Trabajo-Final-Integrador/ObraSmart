package com.obrasmart.identity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private String username;
    private String role;
    private String status;
    private String observaciones;
}
