package com.obrasmart.identity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateRequest {
    @Email
    @NotBlank
    private String email;
    private String firstname;
    private String lastname;
    @NotBlank
    private String username;
    @NotBlank
    private String role;
    @NotBlank
    private String status;
    private String observaciones;
    private String password;
}
