package com.ObraSmart.GestionStock.Controller;

import com.ObraSmart.GestionStock.Service.RepuestoService;
import com.ObraSmart.GestionStock.Dto.RepuestoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repuestos")
public class RepuestoController {

    private final RepuestoService repuestoService;

    @Autowired
    public RepuestoController(RepuestoService repuestoService) {
        this.repuestoService = repuestoService;
    }

    // 🔹 1️⃣ Listar todos
    @GetMapping
    public List<RepuestoDto> getAllRepuestos() {
        return repuestoService.getAllRepuestos();
    }

    // 🔹 2️⃣ Obtener uno por ID
    @GetMapping("/{id}")
    public ResponseEntity<RepuestoDto> getRepuestoById(@PathVariable Long id) {
        RepuestoDto repuesto = repuestoService.getRepuestoById(id);
        return repuesto != null ? ResponseEntity.ok(repuesto) : ResponseEntity.notFound().build();
    }

    // 🔹 3️⃣ Crear nuevo
    @PostMapping
    public RepuestoDto createRepuesto(@RequestBody RepuestoDto repuestoDto) {
        return repuestoService.saveRepuesto(repuestoDto);
    }

    // 🔹 4️⃣ Actualizar existente
    @PutMapping("/{id}")
    public ResponseEntity<RepuestoDto> updateRepuesto(@PathVariable Long id, @RequestBody RepuestoDto repuestoDto) {
        RepuestoDto updated = repuestoService.updateRepuesto(id, repuestoDto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // 🔹 5️⃣ Eliminar (baja lógica)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepuesto(@PathVariable Long id) {
        repuestoService.deleteRepuesto(id);
        return ResponseEntity.noContent().build();
    }

    // 🔹 NUEVOS ENDPOINTS PARA GESTIÓN DE STOCK

    // 🔹 6️⃣ Sacar del stock
    @PostMapping("/{id}/sacar")
    public ResponseEntity<?> sacarDelStock(@PathVariable Long id, @RequestParam int cantidad) {
        try {
            RepuestoDto resultado = repuestoService.sacarDelStock(id, cantidad);
            return ResponseEntity.ok(resultado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 🔹 7️⃣ Agregar al stock
    @PostMapping("/{id}/agregar")
    public ResponseEntity<RepuestoDto> agregarAlStock(@PathVariable Long id, @RequestParam int cantidad) {
        try {
            RepuestoDto resultado = repuestoService.agregarAlStock(id, cantidad);
            return ResponseEntity.ok(resultado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 🔹 8️⃣ Verificar stock disponible
    @GetMapping("/{id}/stock")
    public ResponseEntity<Integer> verificarStock(@PathVariable Long id) {
        try {
            int stock = repuestoService.verificarStockDisponible(id);
            return ResponseEntity.ok(stock);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 🔹 9️⃣ Alertas de stock bajo
    @GetMapping("/alertas-stock")
    public ResponseEntity<List<RepuestoDto>> getAlertasStock(@RequestParam(defaultValue = "5") int stockMinimo) {
        List<RepuestoDto> alertas = repuestoService.getRepuestosStockBajo(stockMinimo);
        return ResponseEntity.ok(alertas);
    }
}


