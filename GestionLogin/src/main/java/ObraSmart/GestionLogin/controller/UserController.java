package ObraSmart.GestionLogin.controller;

import ObraSmart.GestionLogin.dto.CreateUserRequest;
import ObraSmart.GestionLogin.dto.UpdateUserRequest;
import ObraSmart.GestionLogin.dto.UserDTO;
import ObraSmart.GestionLogin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}