package com.ObraSmart.GestionInspeccion.controller;

import com.ObraSmart.GestionInspeccion.dto.CreateEmpleadoRequest;
import com.ObraSmart.GestionInspeccion.dto.EmpleadoDTO;
import com.ObraSmart.GestionInspeccion.dto.UpdateEmpleadoRequest;
import com.ObraSmart.GestionInspeccion.service.EmpleadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class EmpleadoController {

    private final EmpleadoService service;

    @PostMapping
    public ResponseEntity<EmpleadoDTO> crear(
            @Valid @ModelAttribute CreateEmpleadoRequest request,
            @RequestParam("foto") MultipartFile foto) {
        return ResponseEntity.ok(service.crear(request, foto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> actualizar(@PathVariable Long id, @RequestBody UpdateEmpleadoRequest r) {
        return ResponseEntity.ok(service.actualizar(id, r));
    }

    @PatchMapping("/{id}/baja")
    public ResponseEntity<Void> baja(@PathVariable Long id) {
        service.bajaLogica(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<EmpleadoDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<EmpleadoDTO>> buscarPorApellido(@RequestParam String apellido) {
        return ResponseEntity.ok(service.buscarPorApellido(apellido));
    }
}
