package com.ObraSmart.GestionDeLogin.Dto;

import com.ObraSmart.GestionDeLogin.Dto.UserResponseDto;

public class AuthResponseDto {

    private String token;
    private String refreshToken;
    private UserResponseDto user;

    public AuthResponseDto() {}

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRefreshToken() {return refreshToken;}

    public void setRefreshToken(String refreshToken) {this.refreshToken = refreshToken;}

    public UserResponseDto getUser() { return user; }
    public void setUser(UserResponseDto user) { this.user = user; }

}
