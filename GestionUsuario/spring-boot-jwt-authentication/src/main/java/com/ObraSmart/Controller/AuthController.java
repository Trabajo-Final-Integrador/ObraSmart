package com.ObraSmart.Controller;

import com.ObraSmart.Dto.UserRequest;
import com.ObraSmart.Dto.UserResponse;
import com.ObraSmart.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")  // ✅ Esta ruta debe coincidir con la que permitiste en SecurityConfig
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")  // si usás Angular u otro frontend
public class AuthController {

    private final UserService userService;

    /**
     * 📌 Registro de nuevos usuarios
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest userRequest) {
        UserResponse response = userService.createUser(userRequest);
        return ResponseEntity.ok(response);
    }
}
