package com.ObraSmart.GestionReportes.controller;

import lombok.RequiredArgsConstructor;
import com.ObraSmart.GestionReportes.dto.DashboardReporteDto;
import com.ObraSmart.GestionReportes.service.ReporteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor

public class ReporteController {

    private final ReporteService reporteService;

    /**
     * Dashboard "live": solo consulta los otros microservicios.
     */
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardReporteDto> getDashboardLive() {
        DashboardReporteDto dto = reporteService.generarDashboard();
        return ResponseEntity.ok(dto);
    }

    /**
     * Genera y devuelve dashboard.
     * (Tu servicio NO implementa guardado, así que usamos lo mismo)
     */
    @PostMapping("/dashboard")
    public ResponseEntity<DashboardReporteDto> generarYGuardarDashboard() {
        DashboardReporteDto dto = reporteService.generarDashboard();
        return ResponseEntity.ok(dto);
    }

    /**
     * Historial → tu servicio NO lo implementa.
     * Lo desactivo para evitar errores.
     */
    @GetMapping("/dashboard/historial")
    public ResponseEntity<List<DashboardReporteDto>> getHistorial(
            @RequestParam(name = "limit", defaultValue = "10") int limit) {

        return ResponseEntity.status(501).build(); // Not Implemented
    }
}
