package com.ObraSmart.GestionDeLogin.Service;



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
}
