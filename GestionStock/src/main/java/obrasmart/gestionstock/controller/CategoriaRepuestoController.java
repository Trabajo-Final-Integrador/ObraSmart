package obrasmart.gestionstock.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import obrasmart.gestionstock.dto.CategoriaRepuestoDto;
import obrasmart.gestionstock.service.CategoriaRepuestoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaRepuestoController {

    private final CategoriaRepuestoService service;

    @GetMapping
    public ResponseEntity<List<CategoriaRepuestoDto>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaRepuestoDto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaRepuestoDto> crear(@Valid @RequestBody CategoriaRepuestoDto dto) {
        return ResponseEntity.ok(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaRepuestoDto> actualizar(@PathVariable Long id,
                                                           @Valid @RequestBody CategoriaRepuestoDto dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}