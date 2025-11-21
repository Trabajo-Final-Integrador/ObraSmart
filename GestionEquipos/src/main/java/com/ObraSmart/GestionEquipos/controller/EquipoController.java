package com.ObraSmart.GestionEquipos.controller;

import com.ObraSmart.GestionEquipos.dtos.EquipoDTO;
import com.ObraSmart.GestionEquipos.service.EquipoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipos")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EquipoController {

    private final EquipoService equipoService;

    // ============================================================
    // LISTAR TODOS
    // ============================================================
    @GetMapping
    public ResponseEntity<List<EquipoDTO>> getAllEquipos() {
        return ResponseEntity.ok(equipoService.getAll());
    }

    // ============================================================
    // OBTENER POR ID
    // ============================================================
    @GetMapping("/{id}")
    public ResponseEntity<EquipoDTO> getEquipoById(@PathVariable Long id) {
        return ResponseEntity.ok(equipoService.getById(id));
    }

    // ============================================================
    // CREAR
    // ============================================================
    @PostMapping
    public ResponseEntity<EquipoDTO> createEquipo(@Valid @RequestBody EquipoDTO dto) {
        EquipoDTO creado = equipoService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // ============================================================
    // ACTUALIZAR
    // ============================================================
    @PutMapping("/{id}")
    public ResponseEntity<EquipoDTO> updateEquipo(
            @PathVariable Long id,
            @Valid @RequestBody EquipoDTO dto
    ) {
        return ResponseEntity.ok(equipoService.update(id, dto));
    }

    // ============================================================
    // ELIMINAR (BORRADO LÓGICO)
    // ============================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipo(@PathVariable Long id) {
        equipoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
