package com.ObraSmart.GestionDeLogin.Dto;

import com.ObraSmart.GestionDeLogin.Dto.UserResponseDto;

public class AuthResponseDto {

    private String token;
    private UserResponseDto user;

    public AuthResponseDto() {}

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public UserResponseDto getUser() { return user; }
    public void setUser(UserResponseDto user) { this.user = user; }
}
