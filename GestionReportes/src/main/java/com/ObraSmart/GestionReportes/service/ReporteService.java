package com.ObraSmart.GestionReportes.service;

import com.ObraSmart.GestionReportes.dto.DashboardReporteDto;

import java.time.LocalDate;

public interface ReporteService {

    /**
     * Genera el dashboard consolidado de ObraSmart combinando:
     * - Equipos
     * - Reparaciones
     * - Stock / Repuestos
     * Permite acotar por rango de fechas/preset.
     */
    DashboardReporteDto generarDashboard(LocalDate startDate, LocalDate endDate, String preset);

    /**
     * Compatibilidad con llamadas existentes sin filtros.
     */
    default DashboardReporteDto generarDashboard() {
        return generarDashboard(null, null, null);
    }
}
