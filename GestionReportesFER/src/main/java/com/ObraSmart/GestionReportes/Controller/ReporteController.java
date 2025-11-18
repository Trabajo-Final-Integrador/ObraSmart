package com.ObraSmart.GestionReportes.Controller;


import lombok.RequiredArgsConstructor;
import com.ObraSmart.GestionReportes.Dto.DashboardReporteDto;
import com.ObraSmart.GestionReportes.Service.ReporteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReporteController {

    private final ReporteService reporteService;

    /**
     * Dashboard "live": NO guarda en BD, solo consulta los otros microservicios.
     */
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardReporteDto> getDashboardLive() {
        DashboardReporteDto dto = reporteService.generarDashboard(false);
        return ResponseEntity.ok(dto);
    }

    /**
     * Genera dashboard y guarda snapshot en la BD de reportes.
     */
    @PostMapping("/dashboard")
    public ResponseEntity<DashboardReporteDto> generarYGuardarDashboard() {
        DashboardReporteDto dto = reporteService.generarDashboard(true);
        return ResponseEntity.ok(dto);
    }

    /**
     * Historial de snapshots. Parámetro limit opcional.
     */
    @GetMapping("/dashboard/historial")
    public ResponseEntity<List<DashboardReporteDto>> getHistorial(
            @RequestParam(name = "limit", defaultValue = "10") int limit) {

        List<DashboardReporteDto> historial = reporteService.obtenerHistorial(limit);
        return ResponseEntity.ok(historial);
    }
}
