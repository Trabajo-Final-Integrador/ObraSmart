package com.ObraSmart.GestionReportes.service;

import com.ObraSmart.GestionReportes.dto.DashboardReporteDto;

public interface ReporteService {

    /**
     * Genera el dashboard consolidado de ObraSmart
     * combinando información de:
     * - Equipos
     * - Reparaciones
     * - Stock / Repuestos
     */
    DashboardReporteDto generarDashboard();
}
