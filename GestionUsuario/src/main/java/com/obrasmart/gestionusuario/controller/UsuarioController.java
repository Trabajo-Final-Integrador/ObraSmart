package com.obrasmart.gestionusuario.controller;

import com.obrasmart.gestionusuario.config.CurrentUserProvider;
import com.obrasmart.gestionusuario.dto.UsuarioCreateRequest;
import com.obrasmart.gestionusuario.dto.UsuarioResponse;
import com.obrasmart.gestionusuario.dto.UsuarioUpdateRequest;
import com.obrasmart.gestionusuario.entity.Role;
import com.obrasmart.gestionusuario.entity.Status;
import com.obrasmart.gestionusuario.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsuarioController {

    private final UserService userService;
    private final CurrentUserProvider currentUserProvider;

    @PreAuthorize("hasRole('ADMINISTRACION')")
    @PostMapping
    public ResponseEntity<UsuarioResponse> create(@Valid @RequestBody UsuarioCreateRequest request) {
        return ResponseEntity.ok(userService.create(request));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRACION','SUPERVISOR')")
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> list(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) Status status
    ) {
        return ResponseEntity.ok(userService.list(username, email, role, status));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRACION','SUPERVISOR')")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PreAuthorize("hasRole('ADMINISTRACION')")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> update(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @PreAuthorize("hasRole('ADMINISTRACION')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> me() {
        String identifier = currentUserProvider.getCurrentUsernameOrEmail();
        return ResponseEntity.ok(userService.getCurrentUser(identifier));
    }

    @PreAuthorize("hasRole('ADMINISTRACION')")
    @PostMapping(value = "/{id}/licencia/frente", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadFrente(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        userService.uploadLicenciaFrente(id, file);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMINISTRACION')")
    @PostMapping(value = "/{id}/licencia/dorso", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadDorso(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        userService.uploadLicenciaDorso(id, file);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRACION','SUPERVISOR')")
    @GetMapping("/{id}/licencia/frente")
    public ResponseEntity<byte[]> downloadFrente(@PathVariable Long id) {
        byte[] data = userService.descargarLicenciaFrente(id);
        String contentType = userService.getLicenciaContentTypeFrente(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(data);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRACION','SUPERVISOR')")
    @GetMapping("/{id}/licencia/dorso")
    public ResponseEntity<byte[]> downloadDorso(@PathVariable Long id) {
        byte[] data = userService.descargarLicenciaDorso(id);
        String contentType = userService.getLicenciaContentTypeDorso(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(data);
    }
}
