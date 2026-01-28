package com.obrasmart.identity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetRequest {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    private String token;
}
