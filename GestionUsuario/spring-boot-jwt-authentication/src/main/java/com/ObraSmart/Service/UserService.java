package com.ObraSmart.Service;

import com.ObraSmart.Dto.UserDTO;
import com.ObraSmart.Dto.UserRequest;
import com.ObraSmart.Dto.UserResponse;
import com.ObraSmart.Entity.Role;
import com.ObraSmart.Entity.Status;
import com.ObraSmart.Entity.User;
import com.ObraSmart.Repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 👈 para encriptar contraseña

    /**
     * ✅ Obtener todos los usuarios
     */
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .firstname(user.getFirstname())
                        .lastname(user.getLastname())
                        .email(user.getEmail())
                        .role(user.getRole().name())
                        .status(user.getStatus().name())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * ✅ Obtener usuario por ID
     */
    public UserDTO getUser(Integer id) {
        return userRepository.findById(id)
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .firstname(user.getFirstname())
                        .lastname(user.getLastname())
                        .email(user.getEmail())
                        .role(user.getRole().name())
                        .status(user.getStatus().name())
                        .build())
                .orElse(null);
    }

    /**
     * 📝 Crear nuevo usuario
     */
    @Transactional
    public UserResponse createUser(UserRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword()); // 🔐 encriptar

        User user = User.builder()
                .username(request.getUsername())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(encodedPassword) // ✅ guardamos encriptada
                .role(request.getRole() != null ? request.getRole() : Role.TECNICO)
                .status(request.getStatus() != null ? request.getStatus() : Status.ACTIVO)
                .build();

        userRepository.save(user);
        return new UserResponse("✅ Usuario creado correctamente");
    }

    /**
     * ✍️ Actualizar usuario
     */
    @Transactional
    public UserResponse updateUser(UserRequest request) {
        Role role = request.getRole() != null ? request.getRole() : Role.TECNICO;
        Status status = request.getStatus() != null ? request.getStatus() : Status.ACTIVO;

        userRepository.updateUser(
                request.getId(),
                request.getFirstname(),
                request.getLastname(),
                role,
                status
        );
        return new UserResponse("✍️ Usuario actualizado correctamente");
    }

    /**
     * 🗑️ Eliminar usuario
     */
    @Transactional
    public UserResponse deleteUser(Integer id) {
        userRepository.deleteById(id);
        return new UserResponse("🗑️ Usuario eliminado correctamente");
    }
}
