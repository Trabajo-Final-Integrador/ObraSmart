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
public class EquipoController {

    private final EquipoService equipoService;

    @GetMapping
    public ResponseEntity<List<EquipoDTO>> getAllEquipos() {
        List<EquipoDTO> equipos = equipoService.getAll();
        return ResponseEntity.ok(equipos);
    }


    @GetMapping("/{id}")
    public ResponseEntity<EquipoDTO> getEquipoById(@PathVariable Long id) {
        EquipoDTO equipo = equipoService.getById(id);
        return ResponseEntity.ok(equipo);
    }


    @PostMapping
    public ResponseEntity<EquipoDTO> createEquipo(@Valid @RequestBody EquipoDTO dto) {
        EquipoDTO creado = equipoService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }


    @PutMapping("/{id}")
    public ResponseEntity<EquipoDTO> updateEquipo(
            @PathVariable Long id,
            @Valid @RequestBody EquipoDTO dto) {

        EquipoDTO actualizado = equipoService.update(id, dto);
        return ResponseEntity.ok(actualizado);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipo(@PathVariable Long id) {
        equipoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
