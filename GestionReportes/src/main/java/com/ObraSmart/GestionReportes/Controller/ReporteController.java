package com.ObraSmart.GestionReportes.controller;

import com.ObraSmart.GestionReportes.dto.DashboardReporteDto;
import com.ObraSmart.GestionReportes.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    /**
     * Dashboard "live": consulta los otros microservicios con un rango de fechas.
     */
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardReporteDto> getDashboardLive(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String preset
    ) {
        DashboardReporteDto dto = reporteService.generarDashboard(startDate, endDate, preset);
        return ResponseEntity.ok(dto);
    }

    /**
     * Genera y devuelve dashboard (sin persistir).
     */
    @PostMapping("/dashboard")
    public ResponseEntity<DashboardReporteDto> generarYGuardarDashboard(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String preset
    ) {
        DashboardReporteDto dto = reporteService.generarDashboard(startDate, endDate, preset);
        return ResponseEntity.ok(dto);
    }

    /**
     * Historial: no implementado. Se mantiene 501 para evitar errores.
     */
    @GetMapping("/dashboard/historial")
    public ResponseEntity<List<DashboardReporteDto>> getHistorial(
            @RequestParam(name = "limit", defaultValue = "10") int limit) {

        return ResponseEntity.status(501).build(); // Not Implemented
    }
}
