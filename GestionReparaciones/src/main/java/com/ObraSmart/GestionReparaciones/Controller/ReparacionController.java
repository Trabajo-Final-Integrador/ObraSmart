package com.ObraSmart.GestionReparaciones.Controller;


import com.ObraSmart.GestionReparaciones.Dto.ReparacionDto;
import com.ObraSmart.GestionReparaciones.Dto.ReparacionResponseDto;
import com.ObraSmart.GestionReparaciones.Service.ReparacionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reparaciones")
public class ReparacionController {

    private final ReparacionService service;

    public ReparacionController(ReparacionService service) {
        this.service = service;
    }

    // Crear reparación
    @PostMapping
    public ResponseEntity<ReparacionResponseDto> crear(@Valid @RequestBody ReparacionDto dto) {
        ReparacionResponseDto created = service.crearReparacion(dto);
        return ResponseEntity.ok(created);
    }

    // Listar todas
    @GetMapping
    public ResponseEntity<List<ReparacionResponseDto>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    // Obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<ReparacionResponseDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // Podés agregar endpoints: cambiar estado, filtrar por equipo, por estado, etc.
}
