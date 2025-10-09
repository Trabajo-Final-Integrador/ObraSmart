package com.ObraSmart.GestionDeLogin.Controller;

import com.ObraSmart.GestionDeLogin.Dto.AuthResponseDto;
import com.ObraSmart.GestionDeLogin.Dto.LoginRequestDto;
import com.ObraSmart.GestionDeLogin.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody LoginRequestDto dto) {
        return authService.login(dto);
    }
}
