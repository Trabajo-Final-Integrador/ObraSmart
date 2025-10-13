/*package com.ObraSmart.GestionDeLogin.Service;



import com.ObraSmart.GestionDeLogin.Dto.AuthResponseDto;
import com.ObraSmart.GestionDeLogin.Dto.LoginRequestDto;
import com.ObraSmart.GestionDeLogin.Dto.UserResponseDto;
import com.ObraSmart.GestionDeLogin.Entity.User;
import com.ObraSmart.GestionDeLogin.Exception.InvalidCredentialsException;
import com.ObraSmart.GestionDeLogin.Repository.UserRepository;
import com.ObraSmart.GestionDeLogin.Config.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponseDto login(LoginRequestDto dto) {

        // Verificar credenciales
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Credenciales inválidas"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Credenciales inválidas");
        }

        if (user.getStatus() != null && !user.getStatus().equals(com.ObraSmart.GestionDeLogin.Entity.UserStatus.ACTIVO)) {
            throw new InvalidCredentialsException("Usuario inactivo o sin permisos");
        }

        // Generar JWT
        String token = jwtService.generateToken(user);

        // Crear DTO de respuesta
        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setFirstname(user.getFirstname());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        userDto.setStatus(user.getStatus());

        AuthResponseDto response = new AuthResponseDto();
        response.setToken(token);
        response.setUser(userDto);

        return response;
    }
}*/
package com.ObraSmart.GestionDeLogin.Service;

import com.ObraSmart.GestionDeLogin.Config.JwtService;
import com.ObraSmart.GestionDeLogin.Dto.AuthResponseDto;
import com.ObraSmart.GestionDeLogin.Dto.LoginRequestDto;
import com.ObraSmart.GestionDeLogin.Dto.UserResponseDto;
import com.ObraSmart.GestionDeLogin.Entity.User;
import com.ObraSmart.GestionDeLogin.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public AuthResponseDto login(LoginRequestDto dto) {
        // Buscar usuario por nombre
        Optional<User> userOpt = userRepository.findByUsername(dto.getUsername());

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        User user = userOpt.get();

        // Validar contraseña
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        // Generar token JWT
        String token = jwtService.generateAccessToken(user);
        String refresh = jwtService.generateRefreshToken(user);

        // Mapear User a UserResponseDto
        UserResponseDto userDto = new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getFirstname(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getStatus()
        );

        // Construir la respuesta final
        AuthResponseDto response = new AuthResponseDto();
        response.setToken(token);
        response.setUser(userDto);
        response.setRefreshToken(refresh);


        return response;

    }
}
