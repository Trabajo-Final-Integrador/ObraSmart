package com.ObraSmart.GestionReparaciones.controller;

import com.ObraSmart.GestionReparaciones.dto.ReparacionRequestDto;
import com.ObraSmart.GestionReparaciones.dto.ReparacionResponseDto;
import com.ObraSmart.GestionReparaciones.entity.EstadoReparacion;
import com.ObraSmart.GestionReparaciones.service.ReparacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar reparaciones de equipos de obra.
 */
@RestController
@RequestMapping("/reparaciones")
@RequiredArgsConstructor

public class ReparacionController {

    private final ReparacionService reparacionService;

    // 🔹 En un futuro podés tomar el usuario desde el token JWT.
    private Long mockUsuarioActual() {
        return 1L;
    }

    // ================== CREAR ==================
    @PostMapping
    public ResponseEntity<ReparacionResponseDto> crear(@Valid @RequestBody ReparacionRequestDto request) {
        Long usuarioId = mockUsuarioActual();
        ReparacionResponseDto rep = reparacionService.crearReparacion(request, usuarioId);
        return ResponseEntity.ok(rep);
    }

    // ================== OBTENER ==================
    @GetMapping("/{id}")
    public ResponseEntity<ReparacionResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reparacionService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ReparacionResponseDto>> listarTodas() {
        return ResponseEntity.ok(reparacionService.listarTodas());
    }

    @GetMapping("/equipo/{equipoId}")
    public ResponseEntity<List<ReparacionResponseDto>> listarPorEquipo(@PathVariable Long equipoId) {
        return ResponseEntity.ok(reparacionService.listarPorEquipo(equipoId));
    }

    // ================== ACTUALIZAR ==================
    @PutMapping("/{id}")
    public ResponseEntity<ReparacionResponseDto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ReparacionRequestDto request
    ) {
        Long usuarioId = mockUsuarioActual();
        ReparacionResponseDto rep = reparacionService.actualizarReparacion(id, request, usuarioId);
        return ResponseEntity.ok(rep);
    }

    // ================== CAMBIAR ESTADO ==================
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ReparacionResponseDto> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoReparacion nuevoEstado,
            @RequestParam(required = false) String comentario
    ) {
        Long usuarioId = mockUsuarioActual();
        ReparacionResponseDto rep = reparacionService.cambiarEstado(id, nuevoEstado, usuarioId, comentario);
        return ResponseEntity.ok(rep);
    }
}
