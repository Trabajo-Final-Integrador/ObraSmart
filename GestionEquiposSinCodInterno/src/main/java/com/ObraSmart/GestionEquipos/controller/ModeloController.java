package com.ObraSmart.GestionEquipos.controller;

import com.ObraSmart.GestionEquipos.dtos.ModeloDTO;
import com.ObraSmart.GestionEquipos.service.ModeloService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/modelo")
//@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ModeloController {
    private final ModeloService modeloService;
    public ModeloController(ModeloService modeloService) {
        this.modeloService = modeloService;
    }

    @GetMapping
    public ResponseEntity<List<ModeloDTO>> getAll() {
        List<ModeloDTO> modelos = modeloService.getAll();
        return ResponseEntity.ok(modelos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModeloDTO> getById(@PathVariable Long id) {
        ModeloDTO modelo = modeloService.getById(id);
        return ResponseEntity.ok(modelo);
    }

    @PostMapping
    public ResponseEntity<ModeloDTO> create(@Valid @RequestBody ModeloDTO dto) {
        ModeloDTO created = modeloService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModeloDTO> update(@PathVariable Long id, @Valid @RequestBody ModeloDTO dto) {
        ModeloDTO updated = modeloService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        modeloService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
