package com.ObraSmart.Auth;

import com.ObraSmart.Jwt.JwtService;
import com.ObraSmart.User.Role;
import com.ObraSmart.User.Status;
import com.ObraSmart.User.User;
import com.ObraSmart.User.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user=userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token=jwtService.getToken(user);
        return AuthResponse.builder()
            .token(token)
            .build();

    }

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode( request.getPassword()))
            .firstname(request.getFirstname())
            .lastname(request.lastname)
            .status(Status.ACTIVE)
            .role(Role.ADMIN)
            .build();

        userRepository.save(user);

        return AuthResponse.builder()
            .token(jwtService.getToken(user))
            .build();
        
    }

}
