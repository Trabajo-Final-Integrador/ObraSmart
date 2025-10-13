package com.ObraSmart.Controller;

import com.ObraSmart.Dto.UserDTO;
import com.ObraSmart.Dto.UserRequest;
import com.ObraSmart.Dto.UserResponse;
import com.ObraSmart.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;

    /**
     * ✅ Obtener todos los usuarios
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * ✅ Obtener usuario por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Integer id) {
        UserDTO userDTO = userService.getUser(id);
        return (userDTO == null)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(userDTO);
    }

    /**
     * 📝 Crear nuevo usuario
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.createUser(userRequest));
    }

    /**
     * ✍️ Actualizar usuario
     */
    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.updateUser(userRequest));
    }

    /**
     * 🗑️ Eliminar usuario
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }
}

