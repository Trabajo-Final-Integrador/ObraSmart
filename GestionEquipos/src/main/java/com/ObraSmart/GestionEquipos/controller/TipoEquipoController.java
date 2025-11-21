package com.ObraSmart.GestionEquipos.controller;

import com.ObraSmart.GestionEquipos.dtos.TipoEquipoDTO;
import com.ObraSmart.GestionEquipos.service.TipoEquipoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para manejar los tipos de equipo.
 * Incluye CRUD básico y normalización automática del prefijo.
 */
@RestController
@RequestMapping("/api/tipos-equipo")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TipoEquipoController {

    private final TipoEquipoService tipoEquipoService;

    @PostMapping
    public ResponseEntity<TipoEquipoDTO> create(@RequestBody TipoEquipoDTO dto) {
        return ResponseEntity.ok(tipoEquipoService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoEquipoDTO> update(@PathVariable Long id,
                                                @RequestBody TipoEquipoDTO dto) {
        return ResponseEntity.ok(tipoEquipoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tipoEquipoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoEquipoDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(tipoEquipoService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<TipoEquipoDTO>> getAll() {
        return ResponseEntity.ok(tipoEquipoService.getAll());
    }
}
