package com.ObraSmart.GestionEquipos.controller;

import com.ObraSmart.GestionEquipos.dtos.ModeloDTO;
import com.ObraSmart.GestionEquipos.dtos.TipoEquipoDTO;
import com.ObraSmart.GestionEquipos.service.TipoEquipoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipo-equipo")
//@RequiredArgsConstructor

public class TipoEquipoController {
    private final TipoEquipoService tipoEquipoService;
    public TipoEquipoController(TipoEquipoService tipoEquipoService) {
        this.tipoEquipoService = tipoEquipoService;
    }

    @GetMapping
    public ResponseEntity<List<TipoEquipoDTO>> getAll() {
        List<TipoEquipoDTO> tipoEquipo = tipoEquipoService.getAll();
        return ResponseEntity.ok(tipoEquipo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoEquipoDTO> getById(@PathVariable Long id) {
        TipoEquipoDTO tipo = tipoEquipoService.getById(id);
        return ResponseEntity.ok(tipo);
    }

    @PostMapping
    public ResponseEntity<TipoEquipoDTO> create(@Valid @RequestBody TipoEquipoDTO dto) {
        TipoEquipoDTO created = tipoEquipoService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoEquipoDTO> update(@PathVariable Long id, @Valid @RequestBody TipoEquipoDTO dto) {
        TipoEquipoDTO updated = tipoEquipoService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tipoEquipoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
