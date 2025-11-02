package com.ObraSmart.GestionStock.Controller;

import com.ObraSmart.GestionStock.Dto.RepuestoDto;
import com.ObraSmart.GestionStock.Service.RepuestoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/repuestos")
@RequiredArgsConstructor
public class RepuestoController {

    private final RepuestoService repuestoService;

    @GetMapping
    public ResponseEntity<List<RepuestoDto>> listar() {
        return ResponseEntity.ok(repuestoService.getAllRepuestos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepuestoDto> getById(@PathVariable Long id) {
        RepuestoDto dto = repuestoService.getRepuestoById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<RepuestoDto> crear(@RequestBody RepuestoDto dto) {
        RepuestoDto creado = repuestoService.saveRepuesto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepuestoDto> actualizar(@PathVariable Long id, @RequestBody RepuestoDto dto) {
        RepuestoDto actualizado = repuestoService.updateRepuesto(id, dto);
        if (actualizado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        repuestoService.deleteRepuesto(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sacar")
    public ResponseEntity<?> sacar(@PathVariable Long id, @RequestBody CantidadDto body) {
        try {
            RepuestoDto actualizado = repuestoService.sacarDelStock(id, body.getCantidad());
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/agregar")
    public ResponseEntity<?> agregar(@PathVariable Long id, @RequestBody CantidadDto body) {
        try {
            RepuestoDto actualizado = repuestoService.agregarAlStock(id, body.getCantidad());
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/stock-bajo")
    public ResponseEntity<List<RepuestoDto>> stockBajo(@RequestParam(name = "min", defaultValue = "0") int min) {
        return ResponseEntity.ok(repuestoService.getRepuestosStockBajo(min));
    }

    // DTO local para operaciones de cantidad simple (puedes mover a su propio archivo)
    public static class CantidadDto {
        private int cantidad;
        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    }
}
