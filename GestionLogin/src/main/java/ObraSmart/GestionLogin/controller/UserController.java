package ObraSmart.GestionLogin.controller;

import ObraSmart.GestionLogin.dto.CreateUserRequest;
import ObraSmart.GestionLogin.dto.UpdateUserRequest;
import ObraSmart.GestionLogin.dto.UserDTO;
import ObraSmart.GestionLogin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PreAuthorize("hasAuthority('ADMINISTRACION')")
    @PostMapping
    public UserDTO create(@RequestBody CreateUserRequest req) {
        return service.create(req);
    }

    @PreAuthorize("hasAuthority('ADMINISTRACION')")
    @PutMapping("/{id}")
    public UserDTO update(@PathVariable Long id, @RequestBody UpdateUserRequest req) {
        return service.update(id, req);
    }

    @PreAuthorize("hasAuthority('ADMINISTRACION')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteLogical(id);
    }

    @PreAuthorize("hasAuthority('ADMINISTRACION')")
    @GetMapping("/{id}")
    public UserDTO get(@PathVariable Long id) {
        return service.get(id);
    }

    @PreAuthorize("hasAuthority('ADMINISTRACION')")
    @GetMapping
    public List<UserDTO> list() {
        return service.list();
    }

    @GetMapping("/auth/id")
    public ResponseEntity<Map<String, Object>> getCurrentUser(Authentication auth) {
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Map<String, Object> data = new HashMap<>();
        data.put("username", auth.getName());
        data.put("rol", auth.getAuthorities().iterator().next().getAuthority());
        return ResponseEntity.ok(data);
    }

}