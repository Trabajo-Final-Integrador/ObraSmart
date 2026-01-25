package obrasmart.gestionstock.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import obrasmart.gestionstock.dto.RepuestoDto;
import obrasmart.gestionstock.service.RepuestoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repuestos")
@RequiredArgsConstructor
public class RepuestoController {

    private final RepuestoService service;

    @GetMapping public ResponseEntity<List<RepuestoDto>> listar() { return ResponseEntity.ok(service.listar()); }
    @GetMapping("/{id}") public ResponseEntity<RepuestoDto> buscar(@PathVariable Long id) { return ResponseEntity.ok(service.buscar(id)); }
    @PostMapping public ResponseEntity<RepuestoDto> crear(@Valid @RequestBody RepuestoDto dto){ return ResponseEntity.ok(service.crear(dto)); }
    @PutMapping("/{id}") public ResponseEntity<RepuestoDto> actualizar(@PathVariable Long id, @Valid @RequestBody RepuestoDto dto){ return ResponseEntity.ok(service.actualizar(id, dto)); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable Long id){ service.eliminar(id); return ResponseEntity.noContent().build(); }
}