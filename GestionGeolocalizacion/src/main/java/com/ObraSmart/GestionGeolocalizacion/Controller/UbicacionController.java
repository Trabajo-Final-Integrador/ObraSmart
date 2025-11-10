package com.ObraSmart.GestionGeolocalizacion.Controller;

import com.ObraSmart.GestionGeolocalizacion.Dto.UbicacionDto;
import com.ObraSmart.GestionGeolocalizacion.Service.IUbicacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar ubicaciones dentro del sistema ObraSmart.
 * Funciona sin Swagger, ideal para probar con Postman.
 */
@RestController
@RequestMapping("/api/ubicaciones")
public class UbicacionController {

    private final IUbicacionService ubicacionService;

    public UbicacionController(IUbicacionService ubicacionService) {
        this.ubicacionService = ubicacionService;
    }

    // Obtener todas las ubicaciones
    @GetMapping
    public ResponseEntity<List<UbicacionDto>> obtenerTodas() {
        return ResponseEntity.ok(ubicacionService.obtenerTodas());
    }

    // Obtener una ubicación por ID
    @GetMapping("/{id}")
    public ResponseEntity<UbicacionDto> obtenerPorId(@PathVariable Long id) {
        Optional<UbicacionDto> ubicacion = ubicacionService.obtenerPorId(id);
        return ubicacion.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Obtener ubicaciones por estado (ejemplo: EN_SERVICIO, EN_MANTENIMIENTO)
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<UbicacionDto>> obtenerPorEstado(@PathVariable String estado) {
        List<UbicacionDto> ubicaciones = ubicacionService.obtenerPorEstado(estado);
        return ResponseEntity.ok(ubicaciones);
    }

    // Crear una nueva ubicación
    @PostMapping
    public ResponseEntity<UbicacionDto> crear(@RequestBody UbicacionDto dto) {
        UbicacionDto nueva = ubicacionService.crear(dto);
        return ResponseEntity.ok(nueva);
    }

    // Actualizar una ubicación existente
    @PutMapping("/{id}")
    public ResponseEntity<UbicacionDto> actualizar(@PathVariable Long id, @RequestBody UbicacionDto dto) {
        UbicacionDto actualizada = ubicacionService.actualizar(id, dto);
        if (actualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizada);
    }

    // Eliminar una ubicación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ubicacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
